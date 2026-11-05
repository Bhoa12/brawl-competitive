@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.bhoa.brawlcompetitive.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bhoa.brawlcompetitive.data.Brawler
import com.bhoa.brawlcompetitive.data.VisualAsset

@Composable
fun BrawlerDetailScreen(brawler: Brawler, onBack: () -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text(brawler.name) }, navigationIcon = { TextButton(onClick = onBack) { Text("←") } }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        if (!brawler.imageUrl.isNullOrBlank()) AsyncImage(brawler.imageUrl, brawler.name, Modifier.fillMaxWidth().height(220.dp), contentScale = ContentScale.Fit)
                        Text(brawler.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Text(listOfNotNull(brawler.rarity, brawler.brawlerClass).joinToString(" · "))
                        brawler.description?.let { Text(it) }
                    }
                }
            }
            item { AssetSection("Gadgets", brawler.gadgets) }
            item { AssetSection("Habilidades estelares", brawler.starPowers) }
            item {
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Competitivo", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("Las builds, hipercargas, refuerzos y counters exactos se alimentarán desde la capa de meta para evitar mostrar recomendaciones inventadas como datos actuales.")
                    }
                }
            }
        }
    }
}

@Composable
private fun AssetSection(title: String, assets: List<VisualAsset>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        if (assets.isEmpty()) Text("No hay datos disponibles para este elemento.")
        assets.forEach { asset ->
            ElevatedCard(Modifier.fillMaxWidth()) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    if (!asset.imageUrl.isNullOrBlank()) AsyncImage(asset.imageUrl, asset.name, Modifier.size(64.dp), contentScale = ContentScale.Fit)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(asset.name, fontWeight = FontWeight.Bold)
                        asset.description?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
                    }
                }
            }
        }
    }
}