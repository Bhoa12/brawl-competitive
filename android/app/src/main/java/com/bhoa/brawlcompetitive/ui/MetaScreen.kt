package com.bhoa.brawlcompetitive.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bhoa.brawlcompetitive.data.MetaSnapshot

@Composable
fun MetaScreen(snapshot: MetaSnapshot?) {
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Meta competitivo", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        if (snapshot == null || snapshot.entries.isEmpty()) {
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Esperando datos competitivos", fontWeight = FontWeight.Bold)
                    Text("La aplicación no mostrará estadísticas inventadas. Cuando el recolector esté conectado, aquí aparecerán el meta, los filtros y la fecha exacta de actualización.")
                    Text("Tamaño de muestra: " + (snapshot?.sampleSize ?: 0), style = MaterialTheme.typography.labelMedium)
                }
            }
        } else {
            snapshot.entries.forEach { entry ->
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(14.dp)) {
                        Text(entry.brawlerName, fontWeight = FontWeight.Bold)
                        Text("Tier " + entry.tier + " · Meta score " + entry.metaScore)
                    }
                }
            }
        }
    }
}
