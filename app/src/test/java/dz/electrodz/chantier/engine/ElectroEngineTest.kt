package dz.electrodz.chantier.engine

import org.junit.Assert.*
import org.junit.Test

class ElectroEngineTest {

    @Test
    fun `mono current 3000W 220V cos09`() {
        val i = ElectroEngine.current(3000.0, 220.0, false, 0.9)
        assertEquals(15.15, i, 0.05)
    }

    @Test
    fun `tri current 9000W 380V cos08`() {
        val i = ElectroEngine.current(9000.0, 380.0, true, 0.8)
        assertEquals(17.09, i, 0.05)
    }

    @Test
    fun `lighting long run forces bigger section`() {
        val input = SizingInput(3500.0, false, 0.9, 25.0, ConductorMaterial.CU, Insulation.PVC, CircuitUsage.LIGHTING)
        val r = ElectroEngine.sizeCable(input)
        assertTrue(r.ok)
        // 2.5mm2 gives ~3.7% drop > 3% limit, so engine must step up to 4mm2
        assertEquals(2.5, r.sectionByCurrent, 0.0)
        assertEquals(4.0, r.recommendedSection, 0.0)
        assertTrue(r.dropPct <= 3.0)
    }

    @Test
    fun `other usage accepts 25m on 25sqmm`() {
        val input = SizingInput(3500.0, false, 0.9, 25.0, ConductorMaterial.CU, Insulation.PVC, CircuitUsage.OTHER)
        val r = ElectroEngine.sizeCable(input)
        assertTrue(r.ok)
        assertEquals(2.5, r.recommendedSection, 0.0)
        assertTrue(r.dropPct <= 5.0)
    }

    @Test
    fun `breaker recommendation`() {
        assertEquals(16, ElectroEngine.recommendBreaker(15.15))
        assertEquals(20, ElectroEngine.recommendBreaker(17.09))
        assertEquals(10, ElectroEngine.recommendBreaker(9.0))
    }

    @Test
    fun `oversized load fails gracefully`() {
        val input = SizingInput(200_000.0, true, 0.8, 100.0, ConductorMaterial.CU, Insulation.PVC, CircuitUsage.OTHER)
        val r = ElectroEngine.sizeCable(input)
        assertFalse(r.ok)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `zero power rejected`() {
        ElectroEngine.sizeCable(SizingInput(0.0, false, 0.9, 10.0, ConductorMaterial.CU, Insulation.PVC, CircuitUsage.OTHER))
    }

    @Test
    fun `aluminium needs bigger section than copper`() {
        val cu = ElectroEngine.sizeCable(SizingInput(5000.0, false, 0.9, 30.0, ConductorMaterial.CU, Insulation.PVC, CircuitUsage.OTHER))
        val al = ElectroEngine.sizeCable(SizingInput(5000.0, false, 0.9, 30.0, ConductorMaterial.AL, Insulation.PVC, CircuitUsage.OTHER))
        assertTrue(al.recommendedSection >= cu.recommendedSection)
    }
}
