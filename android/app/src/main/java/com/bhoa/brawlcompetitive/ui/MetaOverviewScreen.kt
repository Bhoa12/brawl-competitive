package com.bhoa.brawlcompetitive.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bhoa.brawlcompetitive.data.BrawlRepository
import com.bhoa.brawlcompetitive.data.Brawler
import com.bhoa.brawlcompetitive.data.CompetitiveRepository
import com.bhoa.brawlcompetitive.data.MetaEntry

@Composable
fun MetaOverviewScreen() {
    var brawlers by remember { mutableStateOf<List<Brawler>>(emptyList()) }
    var meta by remember { mutableStateOf<List<MetaEntry>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        brawlers = BrawlRepository.loadBrawlers()
        meta = CompetitiveRepository.loadMeta()
        loading = false
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Meta competitivo", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        if (meta.isEmpty()) {
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp)) {
                    Text("Fuente competitiva en preparación", fontWeight = FontWeight.Bold)
                    Text("Los datos del juego se actualizan automáticamente, pero las estadísticas de uso, victorias y rangos solo aparecerán cuando exista una muestra analítica verificable.")
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        if (loading) Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        else if (meta.isNotEmpty()) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(meta.sortedByDescending { it.winRate ?: 0.0 }) { entry ->
                    ElevatedCard(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(14.dp)) {
                            Text(entry.brawlerName, fontWeight = FontWeight.Bold)
                            Text(listOfNotNull(entry.mode, entry.mapName, entry.rank).joinToString(" · "))
                            entry.pickRate?.let { Text("Uso: " + String.format("%.1f%%", it)) }
                            entry.winRate?.let { Text("Victorias: " + String.format("%.1f%%", it)) }
                            entry.sampleSize?.let { Text("Muestra: " + it) }
                        }
                    }
                }
            }
        } else if (brawlers.isNotEmpty()) {
            Text("Catálogo disponible: " + brawlers.size + " brawlers. El ranking competitivo se activará al conectar el backend de estadísticas.")
        }
    }
}