package com.bhoa.brawlcompetitive.ui

import androidx.compose.foundation.clickable
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
import com.bhoa.brawlcompetitive.data.BrawlRepository
import com.bhoa.brawlcompetitive.data.Brawler
import kotlinx.coroutines.launch

@Composable
fun BrawlerCatalogScreen(onBrawlerSelected: (Brawler) -> Unit) {
    val scope = rememberCoroutineScope()
    var brawlers by remember { mutableStateOf<List<Brawler>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    fun reload() = scope.launch { loading = true; brawlers = BrawlRepository.loadBrawlers(); loading = false }
    LaunchedEffect(Unit) { reload() }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.weight(1f)) {
                Text("Brawlers", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(brawlers.size.toString() + " disponibles · datos actualizables")
            }
            IconButton(onClick = { reload() }) { Text("↻") }
        }
        Spacer(Modifier.height(12.dp))
        if (loading) Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        else LazyVerticalGrid(columns = GridCells.Adaptive(145.dp), verticalArrangement = Arrangement.spacedBy(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(brawlers, key = { it.id }) { brawler ->
                ElevatedCard(Modifier.fillMaxWidth().clickable { onBrawlerSelected(brawler) }) {
                    Column(Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        if (!brawler.imageUrl.isNullOrBlank()) AsyncImage(brawler.imageUrl, brawler.name, Modifier.fillMaxWidth().height(120.dp), contentScale = ContentScale.Fit)
                        else Text("🎮", style = MaterialTheme.typography.displayMedium)
                        Text(brawler.name, fontWeight = FontWeight.Bold)
                        brawler.brawlerClass?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
                    }
                }
            }
        }
    }
}