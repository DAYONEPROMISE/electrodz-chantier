package dz.electrodz.chantier.ui

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.print.PrintAttributes
import android.print.pdf.PrintedPdfDocument
import androidx.core.content.FileProvider
import dz.electrodz.chantier.data.Calculation
import dz.electrodz.chantier.data.Fiche
import java.io.File

object PdfExport {
    fun shareFiche(ctx: Context, fiche: Fiche, calc: Calculation?) {
        val attrs = PrintAttributes.Builder()
            .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
            .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
            .build()
        val doc = PrintedPdfDocument(ctx, attrs)
        try {
            val page = doc.startPage(0)
            val c = page.canvas
            val title = Paint().apply { textSize = 28f; isFakeBoldText = true }
            val body = Paint().apply { textSize = 18f }
            var y = 80f
            val line: (String) -> Unit = { s -> c.drawText(s.take(80), 48f, y, if (y == 80f) title else body); y += 34f }
            line("ElectroDZ - Fiche d'intervention")
            line("Client: ${fiche.clientName}")
            line("Date: ${fiche.date}  Wilaya: ${fiche.wilaya}")
            line("Chantier: ${fiche.siteAddress}")
            line("Travaux: ${fiche.description}")
            line("Fait: ${fiche.tasksDone}")
            if (calc != null) {
                line("--- Dimensionnement ---")
                line("P=${calc.powerW}W ${if (calc.threePhase) "tri 380V" else "mono 220V"} cos=${calc.cosPhi}")
                line("L=${calc.lengthM}m ${calc.material}/${calc.insulation}")
                line("I=${"%.1f".format(calc.currentA)}A S=${calc.sectionMm2}mm2 dU=${"%.2f".format(calc.dropPct)}%")
                line("Disjoncteur: ${calc.breakerA}A")
            }
            doc.finishPage(page)
            val dir = File(ctx.cacheDir, "pdf").apply { mkdirs() }
            val file = File(dir, "fiche-${fiche.id}.pdf")
            file.outputStream().use { doc.writeTo(it) }
            val uri = FileProvider.getUriForFile(ctx, ctx.packageName + ".fileprovider", file)
            val share = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            ctx.startActivity(Intent.createChooser(share, "Partager la fiche"))
        } catch (e: Exception) {
        } finally {
            doc.close()
        }
    }
}
