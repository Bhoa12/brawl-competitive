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
fun CountersScreen() {
    var brawlers by remember { mutableStateOf<List<Brawler>>(emptyList()) }
    var selected by remember { mutableStateOf<Brawler?>(null) }
    var expanded by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { brawlers = BrawlRepository.loadBrawlers() }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Counters", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Selecciona un brawler para consultar enfrentamientos recomendados.")
        Spacer(Modifier.height(12.dp))
        Box {
            OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                Text(selected?.name ?: "Elegir brawler", modifier = Modifier.weight(1f)); Text("▼")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                brawlers.forEach { b -> DropdownMenuItem(text = { Text(b.name) }, onClick = { selected = b; expanded = false }) }
            }
        }
        selected?.let { target ->
            val counters = suggestedCounters(target, brawlers)
            Spacer(Modifier.height(16.dp))
            Text("Amenazas para " + target.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxSize()) {
                items(counters) { enemy ->
                    ElevatedCard(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(14.dp)) {
                            Text(enemy.name, fontWeight = FontWeight.Bold)
                            Text("Counter recomendado: controla la distancia y evita entrar sin ventaja.")
                            Text("La build exacta se mostrará desde la fuente competitiva cuando esté disponible.", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        } ?: run {
            if (brawlers.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            } else {
                Text("Selecciona un brawler para empezar.")
            }
        }
    }
}

private fun suggestedCounters(target: Brawler, all: List<Brawler>): List<Brawler> {
    val tanks = listOf("El Primo","Bull","Frank","Rosa","Jacky","Darryl")
    val shooters = listOf("Piper","Brock","Bea","Mandy","Angelo")
    val throwers = listOf("Tick","Barley","Dynamike","Grom","Sprout")
    val preferred = when {
        target.name in tanks -> listOf("Shelly","Gale","Colette","Emz")
        target.name in shooters -> listOf("Mortis","Edgar","Leon","Kenji")
        target.name in throwers -> listOf("Mortis","Mico","Edgar","Lily")
        else -> listOf("Shelly","Colt","Piper","Edgar")
    }
    return preferred.mapNotNull { name -> all.firstOrNull { it.name.equals(name, true) } }.ifEmpty { all.filter { it.id != target.id }.take(4) }
}