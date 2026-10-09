package dz.electrodz.chantier.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dz.electrodz.chantier.R
import dz.electrodz.chantier.data.Calculation
import dz.electrodz.chantier.data.Client
import dz.electrodz.chantier.data.Fiche
import dz.electrodz.chantier.engine.CircuitUsage
import dz.electrodz.chantier.engine.ConductorMaterial
import dz.electrodz.chantier.engine.ElectroEngine
import dz.electrodz.chantier.engine.Insulation
import dz.electrodz.chantier.engine.SizingInput
import dz.electrodz.chantier.engine.SizingResult
import kotlinx.coroutines.launch

@Composable
private fun ChipRow(options: List<String>, selected: Int, onSelect: (Int) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEachIndexed { i, label ->
            FilterChip(selected = i == selected, onClick = { onSelect(i) }, label = { Text(label) })
        }
    }
}

@Composable
fun CalculatorScreen(vm: AppViewModel) {
    var power by remember { mutableStateOf("3500") }
    var length by remember { mutableStateOf("25") }
    var cos by remember { mutableStateOf("0.9") }
    var tri by remember { mutableStateOf(0) }
    var mat by remember { mutableStateOf(0) }
    var ins by remember { mutableStateOf(0) }
    var use by remember { mutableStateOf(1) }
    var result by remember { mutableStateOf<SizingResult?>(null) }
    var error by remember { mutableStateOf("") }
    var saved by remember { mutableStateOf(false) }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Dimensionnement de câble", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(power, { power = it; saved = false }, label = { Text("Puissance (W)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(length, { length = it; saved = false }, label = { Text("Longueur (m)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(cos, { cos = it; saved = false }, label = { Text("cos φ (0.4–1)") }, modifier = Modifier.fillMaxWidth())
        Text("Réseau"); ChipRow(listOf("Mono 220V", "Tri 380V"), tri, { tri = it })
        Text("Âme"); ChipRow(listOf("Cuivre", "Alu"), mat, { mat = it })
        Text("Isolant"); ChipRow(listOf("PVC", "XLPE"), ins, { ins = it })
        Text("Usage"); ChipRow(listOf("Éclairage (3%)", "Autre (5%)"), use, { use = it })
        if (error.isNotEmpty()) Text(error, color = MaterialTheme.colorScheme.error)
        Button(onClick = {
            error = ""
            saved = false
            try {
                val input = SizingInput(
                    powerW = power.toDouble(), threePhase = tri == 1,
                    cosPhi = cos.toDouble(), lengthM = length.toDouble(),
                    material = if (mat == 0) ConductorMaterial.CU else ConductorMaterial.AL,
                    insulation = if (ins == 0) Insulation.PVC else Insulation.XLPE,
                    usage = if (use == 0) CircuitUsage.LIGHTING else CircuitUsage.OTHER
                )
                result = ElectroEngine.sizeCable(input)
            } catch (e: Exception) {
                error = "Valeurs invalides: ${e.message}"
            }
        }, modifier = Modifier.fillMaxWidth()) { Text("Calculer") }
        result?.let { r ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("I = ${"%.2f".format(r.currentA)} A")
                    if (r.ok) {
                        Text("Section mini (intensité) : ${r.sectionByCurrent} mm²")
                        Text("Section recommandée : ${r.recommendedSection} mm²", style = MaterialTheme.typography.titleMedium)
                        Text("Chute de tension : ${"%.2f".format(r.dropPct)} %")
                        Text("Disjoncteur : ${r.breakerA} A")
                    }
                    Text(r.note)
                    if (r.ok && !saved) {
                        Button(onClick = {
                            vm.saveCalc(
                                Calculation(
                                    powerW = power.toDoubleOrNull() ?: 0.0,
                                    threePhase = tri == 1, cosPhi = cos.toDoubleOrNull() ?: 0.9,
                                    lengthM = length.toDoubleOrNull() ?: 0.0,
                                    material = if (mat == 0) "CU" else "AL",
                                    insulation = if (ins == 0) "PVC" else "XLPE",
                                    usage = if (use == 0) "LIGHTING" else "OTHER",
                                    currentA = r.currentA, sectionMm2 = r.recommendedSection,
                                    dropPct = r.dropPct, breakerA = r.breakerA
                                )
                            ) { saved = true }
                        }) { Text("Enregistrer") }
                    }
                    if (saved) Text("Enregistré dans l'historique")
                }
            }
        }
        Text(stringResource(R.string.disclaimer), style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun ClientsScreen(vm: AppViewModel) {
    val clients by vm.clients.collectAsState()
    var show by remember { mutableStateOf(false) }
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { show = true }) { Icon(Icons.Filled.Add, null) }
    }) { pad ->
        LazyColumn(Modifier.fillMaxSize().padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(clients, key = { it.id }) { c ->
                Card(Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column { Text(c.name, style = MaterialTheme.typography.titleMedium); Text("${c.phone} · ${c.wilaya}") }
                        IconButton(onClick = { vm.deleteClient(c) }) { Icon(Icons.Filled.Delete, null) }
                    }
                }
            }
        }
    }
    if (show) {
        var name by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("") }
        var wilaya by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { show = false }, title = { Text("Nouveau client") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(name, { name = it }, label = { Text("Nom") })
                    OutlinedTextField(phone, { phone = it }, label = { Text("Téléphone") })
                    OutlinedTextField(wilaya, { wilaya = it }, label = { Text("Wilaya") })
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (name.isNotBlank()) { vm.addClient(Client(name = name, phone = phone, wilaya = wilaya)); show = false }
                }) { Text("Ajouter") }
            },
            dismissButton = { TextButton(onClick = { show = false }) { Text("Annuler") } }
        )
    }
}

@Composable
fun FichesScreen(vm: AppViewModel) {
    val fiches by vm.fiches.collectAsState()
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    var show by remember { mutableStateOf(false) }
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { show = true }) { Icon(Icons.Filled.Add, null) }
    }) { pad ->
        LazyColumn(Modifier.fillMaxSize().padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(fiches, key = { it.id }) { f ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        Text(f.clientName.ifBlank { "Sans client" }, style = MaterialTheme.typography.titleMedium)
                        Text("${f.date} · ${f.wilaya}")
                        if (f.description.isNotBlank()) Text(f.description)
                        Row {
                            IconButton(onClick = {
                                scope.launch {
                                    val calc = if (f.calcId > 0) vm.calcById(f.calcId) else null
                                    try { PdfExport.shareFiche(ctx, f, calc) } catch (_: Exception) { }
                                }
                            }) { Icon(Icons.Filled.Share, null) }
                            IconButton(onClick = { vm.deleteFiche(f) }) { Icon(Icons.Filled.Delete, null) }
                        }
                    }
                }
            }
        }
    }
    if (show) {
        var client by remember { mutableStateOf("") }
        var date by remember { mutableStateOf("") }
        var site by remember { mutableStateOf("") }
        var wilaya by remember { mutableStateOf("") }
        var desc by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { show = false }, title = { Text("Nouvelle fiche") },
            text = {
                Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(client, { client = it }, label = { Text("Client") })
                    OutlinedTextField(date, { date = it }, label = { Text("Date") })
                    OutlinedTextField(site, { site = it }, label = { Text("Adresse chantier") })
                    OutlinedTextField(wilaya, { wilaya = it }, label = { Text("Wilaya") })
                    OutlinedTextField(desc, { desc = it }, label = { Text("Travaux réalisés") })
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    vm.addFiche(Fiche(clientName = client, date = date, siteAddress = site, wilaya = wilaya, description = desc))
                    show = false
                }) { Text("Ajouter") }
            },
            dismissButton = { TextButton(onClick = { show = false }) { Text("Annuler") } }
        )
    }
}

@Composable
fun HistoryScreen(vm: AppViewModel) {
    val calcs by vm.calcs.collectAsState()
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(calcs, key = { it.id }) { c ->
            Card(Modifier.fillMaxWidth()) {
                Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("${c.powerW.toInt()}W ${if (c.threePhase) "tri" else "mono"} · ${c.lengthM.toInt()}m ${c.material}", style = MaterialTheme.typography.titleSmall)
                        Text("S=${c.sectionMm2}mm² I=${"%.1f".format(c.currentA)}A ΔU=${"%.2f".format(c.dropPct)}% DJ=${c.breakerA}A")
                    }
                    IconButton(onClick = { vm.deleteCalc(c.id) }) { Icon(Icons.Filled.Delete, null) }
                }
            }
        }
    }
}
