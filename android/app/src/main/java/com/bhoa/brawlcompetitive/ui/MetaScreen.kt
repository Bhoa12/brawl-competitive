package com.bhoa.brawlcompetitive.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bhoa.brawlcompetitive.data.MetaSnapshot

data class CompetitiveFilterOption(val id: Int? = null, val name: String, val imageUrl: String? = null)

@Composable
fun CompetitiveMetaScreen(
    snapshot: MetaSnapshot?,
    maps: List<CompetitiveFilterOption>,
    modes: List<String>,
    ranks: List<String>,
    subranks: List<String>,
    onFiltersChanged: (mapId: Int?, mode: String?, rank: String?, subrank: String?) -> Unit
) {
    var selectedMap by remember { mutableStateOf<CompetitiveFilterOption?>(null) }
    var selectedMode by remember { mutableStateOf<String?>(null) }
    var selectedRank by remember { mutableStateOf<String?>(null) }
    var selectedSubrank by remember { mutableStateOf<String?>(null) }

    fun changed() = onFiltersChanged(selectedMap?.id, selectedMode, selectedRank, selectedSubrank)

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Meta competitivo", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Selecciona el contexto competitivo y consulta datos reales.")

        FilterSelector("Mapa", selectedMap?.name ?: "Todos", maps.map { it.name }) {
            selectedMap = maps.firstOrNull { option -> option.name == it }; changed()
        }
        FilterSelector("Modo", selectedMode ?: "Todos", modes) { selectedMode = it; changed() }
        FilterSelector("Rango", selectedRank ?: "Todos", ranks) {
            selectedRank = it; selectedSubrank = null; changed()
        }
        FilterSelector("Subrango", selectedSubrank ?: "Todos", subranks) { selectedSubrank = it; changed() }

        snapshot?.let {
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp)) {
                    Text("Datos del análisis", fontWeight = FontWeight.Bold)
                    Text("Parche: " + it.patch + " · Muestra: " + it.sampleSize)
                    Text("Actualizado: " + it.updatedAt, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
private fun FilterSelector(label: String, selected: String, options: List<String>, onSelect: (String?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Text(label, fontWeight = FontWeight.Bold)
        Box {
            OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                Text(selected, modifier = Modifier.weight(1f)); Text("▼")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(text = { Text("Todos") }, onClick = { expanded = false; onSelect(null) })
                options.forEach { option -> DropdownMenuItem(text = { Text(option) }, onClick = { expanded = false; onSelect(option) }) }
            }
        }
    }
}

@Composable
fun MetaScreen(snapshot: MetaSnapshot?) {
    val viewModel: CompetitiveMetaViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    var selectedMapId by remember { mutableStateOf<Int?>(null) }
    var selectedMode by remember { mutableStateOf<String?>(null) }
    var selectedRank by remember { mutableStateOf<String?>(null) }
    var selectedSubrank by remember { mutableStateOf<String?>(null) }

    if (state.loading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
    } else if (state.response != null) {
        MetaRankingScreen(response = state.response!!)
    } else {
        Column {
            CompetitiveMetaScreen(
                snapshot = snapshot,
                maps = emptyList(),
                modes = listOf("Atrapagemas", "Balón Brawl", "Noqueo", "Atraco", "Caza Estelar"),
                ranks = listOf("Bronce", "Plata", "Oro", "Diamante", "Mítico", "Legendario", "Maestro"),
                subranks = listOf("I", "II", "III"),
                onFiltersChanged = { mapId, mode, rank, subrank ->
                    selectedMapId = mapId; selectedMode = mode; selectedRank = rank; selectedSubrank = subrank
                }
            )
            Button(
                onClick = { viewModel.refresh(selectedMapId, selectedMode, selectedRank, selectedSubrank) },
                modifier = Modifier.padding(16.dp).fillMaxWidth()
            ) { Text("Consultar meta") }
            state.error?.let { Text(it, modifier = Modifier.padding(16.dp)) }
        }
    }
}
