package com.bhoa.brawlcompetitive.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bhoa.brawlcompetitive.data.CompetitiveBuild
import com.bhoa.brawlcompetitive.data.VisualAsset

@Composable
fun BuildCard(build: CompetitiveBuild) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Build recomendada", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            build.gadget?.let { BuildItem("Gadget", it) }
            build.starPower?.let { BuildItem("Estelar", it) }
            build.gear1?.let { BuildItem("Refuerzo 1", it) }
            build.gear2?.let { BuildItem("Refuerzo 2", it) }
            build.hypercharge?.let { BuildItem("Hipercarga", it) }
            build.patch?.let { Text("Versión: $it", style = MaterialTheme.typography.labelSmall) }
        }
    }
}

@Composable
private fun BuildItem(label: String, asset: VisualAsset) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        if (asset.imageUrl != null) {
            AsyncImage(asset.imageUrl, asset.name, Modifier.size(48.dp))
        }
        Column {
            Text(label, style = MaterialTheme.typography.labelMedium)
            Text(asset.name, fontWeight = FontWeight.Bold)
        }
    }
}
