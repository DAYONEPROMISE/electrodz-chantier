package dz.electrodz.chantier.engine

import kotlin.math.sqrt

enum class ConductorMaterial { CU, AL }
enum class Insulation { PVC, XLPE }
enum class CircuitUsage { LIGHTING, OTHER }

data class SizingInput(
    val powerW: Double,
    val threePhase: Boolean,
    val cosPhi: Double,
    val lengthM: Double,
    val material: ConductorMaterial,
    val insulation: Insulation,
    val usage: CircuitUsage
) {
    val voltage: Double get() = if (threePhase) 380.0 else 220.0
    val dropLimitPct: Double get() = if (usage == CircuitUsage.LIGHTING) 3.0 else 5.0
}

data class SizingResult(
    val currentA: Double,
    val sectionByCurrent: Double,
    val recommendedSection: Double,
    val dropPct: Double,
    val breakerA: Int,
    val ok: Boolean,
    val note: String
)

/**
 * Indicative cable-sizing engine (IEC 60364 / C 15-100 method).
 * Ampacities are simplified reference values for common installation conditions;
 * results must be verified against the official code before installation.
 */
object ElectroEngine {

    val STANDARD_SECTIONS = listOf(1.5, 2.5, 4.0, 6.0, 10.0, 16.0, 25.0, 35.0, 50.0, 70.0, 95.0, 120.0)
    val BREAKER_RATINGS = listOf(10, 16, 20, 25, 32, 40, 50, 63, 80, 100, 125)

    private const val RHO_CU = 0.0231 // ohm.mm2/m at operating temperature
    private const val RHO_AL = 0.0378

    // Base ampacity (A) for copper, PVC, 2 loaded conductors, reference conditions.
    private val CU_PVC_BASE = mapOf(
        1.5 to 15.5, 2.5 to 21.0, 4.0 to 28.0, 6.0 to 36.0, 10.0 to 50.0,
        16.0 to 66.0, 25.0 to 84.0, 35.0 to 104.0, 50.0 to 125.0,
        70.0 to 160.0, 95.0 to 192.0, 120.0 to 220.0
    )

    fun current(powerW: Double, voltage: Double, threePhase: Boolean, cosPhi: Double): Double {
        require(powerW > 0) { "power must be positive" }
        require(voltage > 0) { "voltage must be positive" }
        require(cosPhi in 0.4..1.0) { "cosPhi out of range" }
        return if (threePhase) powerW / (sqrt(3.0) * voltage * cosPhi)
        else powerW / (voltage * cosPhi)
    }

    fun ampacity(section: Double, threePhase: Boolean, material: ConductorMaterial, insulation: Insulation): Double {
        val base = CU_PVC_BASE[section] ?: return 0.0
        var v = base
        if (threePhase) v *= 0.9
        if (material == ConductorMaterial.AL) v *= 0.75
        if (insulation == Insulation.XLPE) v *= 1.15
        return v
    }

    fun voltageDropPct(currentA: Double, lengthM: Double, section: Double, input: SizingInput): Double {
        val rho = if (input.material == ConductorMaterial.CU) RHO_CU else RHO_AL
        val drop = if (input.threePhase) {
            sqrt(3.0) * currentA * lengthM * (rho / section) * input.cosPhi
        } else {
            2.0 * currentA * lengthM * (rho / section)
        }
        return drop / input.voltage * 100.0
    }

    fun recommendBreaker(currentA: Double): Int =
        BREAKER_RATINGS.firstOrNull { it >= currentA } ?: -1

    fun sizeCable(input: SizingInput): SizingResult {
        require(input.powerW > 0 && input.powerW <= 250_000) { "power out of range" }
        require(input.lengthM > 0 && input.lengthM <= 2000) { "length out of range" }
        val currentA = current(input.powerW, input.voltage, input.threePhase, input.cosPhi)
        val byCurrent = STANDARD_SECTIONS.firstOrNull {
            ampacity(it, input.threePhase, input.material, input.insulation) >= currentA
        } ?: return SizingResult(currentA, -1.0, -1.0, -1.0, -1, false,
            "Courant trop élevé pour les sections standard — diviser le circuit.")
        var chosen = byCurrent
        for (s in STANDARD_SECTIONS) {
            if (s < byCurrent) continue
            if (voltageDropPct(currentA, input.lengthM, s, input) <= input.dropLimitPct) {
                chosen = s
                break
            }
            chosen = s
        }
        val drop = voltageDropPct(currentA, input.lengthM, chosen, input)
        val breaker = recommendBreaker(currentA)
        val ok = drop <= input.dropLimitPct && breaker > 0
        val note = if (ok) "OK — vérifier selon C 15-100 / IEC 60364 avant installation."
        else "Chute de tension excessive même en 120 mm² — rapprocher la source ou diviser le circuit."
        return SizingResult(currentA, byCurrent, chosen, drop, breaker, ok, note)
    }
}
