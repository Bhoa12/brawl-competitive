package com.bhoa.brawlcompetitive.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bhoa.brawlcompetitive.data.MetaSnapshot

data class CompetitiveFilterOption(val name: String, val imageUrl: String? = null)

@Composable
fun CompetitiveMetaScreen(
    snapshot: MetaSnapshot?,
    maps: List<CompetitiveFilterOption>,
    modes: List<String>,
    ranks: List<String>,
    subranks: List<String>,
    onFiltersChanged: (map: String?, mode: String?, rank: String?, subrank: String?) -> Unit = { _, _, _, _ -> }
) {
    var selectedMap by remember { mutableStateOf<CompetitiveFilterOption?>(null) }
    var selectedMode by remember { mutableStateOf<String?>(null) }
    var selectedRank by remember { mutableStateOf<String?>(null) }
    var selectedSubrank by remember { mutableStateOf<String?>(null) }

    fun changed() = onFiltersChanged(selectedMap?.name, selectedMode, selectedRank, selectedSubrank)

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Meta competitivo", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Filtra el análisis antes de consultar las estadísticas.", style = MaterialTheme.typography.bodyMedium)

        FilterSelector("Mapa", selectedMap?.name ?: "Todos", maps.map { it.name }) {
            selectedMap = maps.firstOrNull { option -> option.name == it }
            changed()
        }
        FilterSelector("Modo", selectedMode ?: "Todos", modes) {
            selectedMode = it
            changed()
        }
        FilterSelector("Rango", selectedRank ?: "Todos", ranks) {
            selectedRank = it
            selectedSubrank = null
            changed()
        }
        FilterSelector("Subrango", selectedSubrank ?: "Todos", subranks) {
            selectedSubrank = it
            changed()
        }

        snapshot?.let {
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp)) {
                    Text("Datos del análisis", fontWeight = FontWeight.Bold)
                    Text("Parche: " + it.patch + " · Muestra: " + it.sampleSize)
                    Text("Actualizado: " + it.updatedAt, style = MaterialTheme.typography.labelSmall)
                }
            }
        } ?: ElevatedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(14.dp)) {
                Text("Esperando datos del contexto seleccionado", fontWeight = FontWeight.Bold)
                Text("No se mostrarán estadísticas inventadas.")
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
                Text(selected, modifier = Modifier.weight(1f))
                Text("▼")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(text = { Text("Todos") }, onClick = { expanded = false; onSelect(null) })
                options.forEach { option ->
                    DropdownMenuItem(text = { Text(option) }, onClick = { expanded = false; onSelect(option) })
                }
            }
        }
    }
}

@Composable
fun MetaScreen(snapshot: MetaSnapshot?) {
    CompetitiveMetaScreen(
        snapshot = snapshot,
        maps = emptyList(),
        modes = listOf("Atrapagemas", "Balón Brawl", "Noqueo", "Atraco", "Caza Estelar"),
        ranks = listOf("Bronce", "Plata", "Oro", "Diamante", "Mítico", "Legendario", "Maestro"),
        subranks = listOf("I", "II", "III")
    )
}
