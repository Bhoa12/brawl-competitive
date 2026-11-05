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

@Composable
fun MetaOverviewScreen() {
    var brawlers by remember { mutableStateOf<List<Brawler>>(emptyList()) }
    LaunchedEffect(Unit) { brawlers = BrawlRepository.loadBrawlers() }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Meta competitivo", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Vista general. Las estadísticas por rango y mapa requieren una fuente analítica configurada.")
        Spacer(Modifier.height(12.dp))
        if (brawlers.isEmpty()) Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        else LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(brawlers.take(12)) { b ->
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("★", style = MaterialTheme.typography.titleLarge); Spacer(Modifier.width(12.dp))
                        Column { Text(b.name, fontWeight = FontWeight.Bold); Text(b.brawlerClass ?: "Brawler") }
                    }
                }
            }
        }
    }
}