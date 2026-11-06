package com.bhoa.brawlcompetitive.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bhoa.brawlcompetitive.data.CompetitiveBuild

@Composable
fun BuildCard(build: CompetitiveBuild) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Build recomendada", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            build.gadget?.let { BuildTextItem("Gadget", it) }
            build.starPower?.let { BuildTextItem("Estelar", it) }
            build.gears.forEachIndexed { index, gear -> BuildTextItem("Refuerzo " + (index + 1), gear) }
            if (build.hypercharge) BuildTextItem("Hipercarga", "Disponible")
            Text(build.source, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun BuildTextItem(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelMedium)
        Text(value, fontWeight = FontWeight.Bold)
    }
}
