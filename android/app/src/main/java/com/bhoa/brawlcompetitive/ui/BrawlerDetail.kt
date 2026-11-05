package com.bhoa.brawlcompetitive.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bhoa.brawlcompetitive.data.Brawler
import com.bhoa.brawlcompetitive.data.VisualAsset

@Composable
fun BrawlerDetailScreen(brawler: Brawler, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(brawler.name) },
                navigationIcon = { TextButton(onClick = onBack) { Text("Volver") } }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                AsyncImage(
                    model = brawler.imageUrl,
                    contentDescription = brawler.name,
                    modifier = Modifier.fillMaxWidth().height(280.dp),
                    contentScale = ContentScale.Fit
                )
            }
            item {
                Text(brawler.name, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                Text(listOfNotNull(brawler.brawlerClass, brawler.rarity).joinToString(" · "))
                brawler.description?.let { Text(it) }
            }
            item { Text("Gadgets", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
            items(brawler.gadgets) { AssetRow(it) }
            item { Text("Habilidades estelares", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
            items(brawler.starPowers) { AssetRow(it) }
            item {
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Próximamente", fontWeight = FontWeight.Bold)
                        Text("Meta por mapa, builds completas, refuerzos, hipercarga y counters directos.")
                    }
                }
            }
        }
    }
}

@Composable
private fun AssetRow(asset: VisualAsset) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AsyncImage(asset.imageUrl, asset.name, Modifier.size(64.dp), contentScale = ContentScale.Fit)
            Column {
                Text(asset.name, fontWeight = FontWeight.Bold)
                asset.description?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            }
        }
    }
}
