package com.bhoa.brawlcompetitive.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bhoa.brawlcompetitive.data.BrawlRepository
import com.bhoa.brawlcompetitive.data.Brawler
import com.bhoa.brawlcompetitive.data.CompetitiveRepository
import com.bhoa.brawlcompetitive.data.MetaEntry
import com.bhoa.brawlcompetitive.data.MetaSyncStore
import java.text.DateFormat
import java.util.Date

@Composable
fun MetaOverviewScreen() {
    val context = LocalContext.current
    var brawlers by remember { mutableStateOf<List<Brawler>>(emptyList()) }
    var meta by remember { mutableStateOf(MetaSyncStore.load(context)?.entries ?: emptyList()) }
    var cachedAt by remember { mutableStateOf(MetaSyncStore.load(context)?.savedAt ?: 0L) }
    var loading by remember { mutableStateOf(true) }
    var syncing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    suspend fun sync() {
        syncing = true
        val fresh = CompetitiveRepository.loadMeta()
        if (fresh.isNotEmpty()) {
            meta = fresh
            MetaSyncStore.save(context, fresh)
            cachedAt = System.currentTimeMillis()
        }
        syncing = false
    }

    LaunchedEffect(Unit) {
        brawlers = BrawlRepository.loadBrawlers()
        sync()
        loading = false
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Meta competitivo", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(if (cachedAt > 0) "Última sincronización: " + DateFormat.getDateTimeInstance().format(Date(cachedAt)) else "Sin datos competitivos descargados")
            Spacer(Modifier.weight(1f))
            TextButton(enabled = !syncing, onClick = { scope.launch { sync() } }) {
                Text(if (syncing) "Sincronizando…" else "Actualizar")
            }
        }
        Spacer(Modifier.height(8.dp))
        if (meta.isEmpty()) {
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp)) {
                    Text("Fuente competitiva en preparación", fontWeight = FontWeight.Bold)
                    Text("La app conserva automáticamente los últimos datos válidos. Las estadísticas solo aparecerán cuando el backend publique una muestra verificable.")
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
            Text("Catálogo disponible: " + brawlers.size + " brawlers. El ranking competitivo se activará automáticamente cuando el backend publique datos verificables.")
        }
    }
}