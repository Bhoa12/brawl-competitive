package com.bhoa.brawlcompetitive.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bhoa.brawlcompetitive.data.BrawlMap
import com.bhoa.brawlcompetitive.data.MapRepository
import kotlinx.coroutines.launch

@Composable
fun MapCatalogScreen() {
    val scope = rememberCoroutineScope()
    var maps by remember { mutableStateOf<List<BrawlMap>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    fun reload() = scope.launch { loading = true; maps = MapRepository.loadMaps(); loading = false }
    LaunchedEffect(Unit) { reload() }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.weight(1f)) {
                Text("Mapas", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(maps.size.toString() + " mapas disponibles")
            }
            IconButton(onClick = { reload() }) { Text("↻") }
        }
        Spacer(Modifier.height(12.dp))
        if (loading) Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        else if (maps.isEmpty()) Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("No se pudieron cargar los mapas.") }
        else LazyVerticalGrid(columns = GridCells.Adaptive(160.dp), verticalArrangement = Arrangement.spacedBy(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(maps, key = { it.id }) { map ->
                ElevatedCard {
                    Column(Modifier.padding(10.dp)) {
                        if (!map.imageUrl.isNullOrBlank()) AsyncImage(map.imageUrl, map.name, Modifier.fillMaxWidth().height(110.dp), contentScale = ContentScale.Crop)
                        Text(map.name, fontWeight = FontWeight.Bold)
                        map.mode?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
                    }
                }
            }
        }
    }
}