@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.bhoa.brawlcompetitive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bhoa.brawlcompetitive.ui.MetaScreen
import coil3.compose.AsyncImage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { BrawlCompetitiveApp() }
    }
}

private enum class Section(val title: String) {
    HOME("Inicio"), META("Meta"), MAPS("Mapas"), BRAWLERS("Brawlers"), COUNTERS("Counters"), PROFILE("Perfil")
}

@Composable
fun BrawlCompetitiveApp() {
    var section by remember { mutableStateOf(Section.HOME) }
    MaterialTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Brawl Competitive", fontWeight = FontWeight.Bold) }) },
            bottomBar = {
                NavigationBar {
                    listOf(Section.HOME, Section.META, Section.MAPS, Section.BRAWLERS, Section.COUNTERS, Section.PROFILE).forEach { item ->
                        NavigationBarItem(
                            selected = section == item,
                            onClick = { section = item },
                            icon = { Icon(sectionIcon(item), contentDescription = item.title) },
                            label = { Text(item.title) }
                        )
                    }
                }
            }
        ) { padding ->
            Box(Modifier.padding(padding).fillMaxSize()) {
                when (section) {
                    Section.HOME -> HomeScreen { section = it }
                    Section.BRAWLERS -> PlaceholderScreen("Brawlers", "El catálogo completo se cargará desde la fuente de datos configurada.")
                    Section.MAPS -> CatalogScreen("Mapas", demoMaps)
                    Section.META -> MetaScreen()
                    Section.COUNTERS -> PlaceholderScreen("Counters", "Elige un brawler y consulta sus enfrentamientos.")
                    Section.PROFILE -> PlaceholderScreen("Perfil", "Busca un jugador mediante su tag cuando conectemos la API oficial.")
                }
            }
        }
    }
}

@Composable private fun HomeScreen(onNavigate: (Section) -> Unit) {
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Centro competitivo", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Meta, mapas, brawlers, counters y preparación competitiva.")
        ElevatedCard(Modifier.fillMaxWidth().clickable { onNavigate(Section.BRAWLERS) }) {
            Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, null, Modifier.size(42.dp))
                Spacer(Modifier.width(14.dp))
                Column { Text("Brawlers", fontWeight = FontWeight.Bold); Text("Imagen + nombre + datos competitivos") }
            }
        }
        ElevatedCard(Modifier.fillMaxWidth().clickable { onNavigate(Section.MAPS) }) {
            Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Place, null, Modifier.size(42.dp))
                Spacer(Modifier.width(14.dp))
                Column { Text("Mapas", fontWeight = FontWeight.Bold); Text("Imagen + nombre + mejores picks") }
            }
        }
    }
}

@Composable private fun CatalogScreen(title: String, entries: List<VisualEntry>) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        LazyVerticalGrid(columns = GridCells.Adaptive(150.dp), verticalArrangement = Arrangement.spacedBy(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(entries) { entry ->
                ElevatedCard {
                    Column {
                        AsyncImage(model = entry.image, contentDescription = entry.name, modifier = Modifier.fillMaxWidth().height(120.dp), contentScale = ContentScale.Crop)
                        Text(entry.name, Modifier.padding(12.dp), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable private fun PlaceholderScreen(title: String, text: String) {
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center) {
        Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(10.dp))
        Text(text)
    }
}

private fun sectionIcon(section: Section) = when (section) {
    Section.HOME -> Icons.Default.Home
    Section.META -> Icons.Default.Star
    Section.MAPS -> Icons.Default.Place
    Section.BRAWLERS -> Icons.Default.Person
    Section.COUNTERS -> Icons.Default.Shield
    Section.PROFILE -> Icons.Default.AccountCircle
}

private data class VisualEntry(val name: String, val image: String)
private val demoMaps = listOf(
    VisualEntry("Mapas competitivos", "https://cdn.brawlify.com/maps/15000000.png")
)
