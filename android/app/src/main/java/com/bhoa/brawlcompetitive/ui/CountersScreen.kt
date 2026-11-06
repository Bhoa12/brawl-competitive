package com.bhoa.brawlcompetitive.ui

import androidx.compose.foundation.layout.*
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
        Text("Los counters competitivos se muestran únicamente cuando existe una muestra verificable de enfrentamientos.")
        Spacer(Modifier.height(12.dp))
        Box {
            OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                Text(selected?.name ?: "Elegir brawler", modifier = Modifier.weight(1f))
                Text("▼")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                brawlers.forEach { b ->
                    DropdownMenuItem(
                        text = { Text(b.name) },
                        onClick = { selected = b; expanded = false }
                    )
                }
            }
        }

        selected?.let { target ->
            Spacer(Modifier.height(16.dp))
            Text("Counters para " + target.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Datos aún no disponibles", fontWeight = FontWeight.Bold)
                    Text("El Competitive Data Hub todavía no dispone de suficientes enfrentamientos observados para recomendar counters con estadísticas reales.")
                    Text("Cuando haya una muestra verificable, aquí se mostrarán win rate, tamaño de muestra, parche y fecha de actualización.", style = MaterialTheme.typography.bodySmall)
                }
            }
        } ?: run {
            if (brawlers.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            } else {
                Spacer(Modifier.height(16.dp))
                Text("Selecciona un brawler para empezar.")
            }
        }
    }
}
