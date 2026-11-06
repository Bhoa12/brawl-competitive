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

data class TacticalCounter(val name: String, val reason: String)

private val baselineCounters = mapOf(
    "Shelly" to listOf(TacticalCounter("Spike", "Puede desgastar a Shelly antes de que consiga entrar en rango."), TacticalCounter("Gale", "Su control dificulta que Shelly cierre la distancia.")),
    "Frank" to listOf(TacticalCounter("Colette", "El daño porcentual es especialmente eficaz contra objetivos con mucha vida."), TacticalCounter("Spike", "Puede desgastar a Frank y castigarlo mientras prepara sus ataques.")),
    "Mortis" to listOf(TacticalCounter("Shelly", "El daño a corta distancia castiga sus entradas."), TacticalCounter("Gale", "El control puede interrumpir y alejar a Mortis."))
)

@Composable
fun CountersScreen() {
    var brawlers by remember { mutableStateOf<List<Brawler>>(emptyList()) }
    var selected by remember { mutableStateOf<Brawler?>(null) }
    var expanded by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { brawlers = BrawlRepository.loadBrawlers() }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Counters", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Recomendaciones tácticas y, cuando exista una muestra verificable, estadísticas competitivas.")
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
            Spacer(Modifier.height(16.dp))
            Text("Counters para " + target.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            val counters = baselineCounters[target.name].orEmpty()
            if (counters.isEmpty()) {
                ElevatedCard(Modifier.fillMaxWidth()) { Column(Modifier.padding(14.dp)) {
                    Text("Aún no hay recomendaciones verificadas", fontWeight = FontWeight.Bold)
                    Text("No se mostrarán counters inventados. Esta ficha se completará cuando el Data Hub disponga de evidencia suficiente.")
                }}
            } else counters.forEach { counter ->
                ElevatedCard(Modifier.fillMaxWidth().padding(top = 8.dp)) { Column(Modifier.padding(14.dp)) {
                    Text(counter.name, fontWeight = FontWeight.Bold); Text(counter.reason)
                    Text("Análisis táctico · Sin win rate verificado todavía", style = MaterialTheme.typography.bodySmall)
                }}
            }
        } ?: if (brawlers.isEmpty()) Box(Modifier.fillMaxSize(), contentAlignment=Alignment.Center){CircularProgressIndicator()}
        else { Spacer(Modifier.height(16.dp)); Text("Selecciona un brawler para empezar.") }
    }
}
