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
import com.bhoa.brawlcompetitive.ui.BrawlerCatalogScreen
import com.bhoa.brawlcompetitive.ui.BrawlerDetailScreen
import com.bhoa.brawlcompetitive.ui.MapCatalogScreen
import com.bhoa.brawlcompetitive.ui.MapDetailScreen
import com.bhoa.brawlcompetitive.data.BrawlMap
import com.bhoa.brawlcompetitive.ui.CountersScreen
import com.bhoa.brawlcompetitive.ui.MetaOverviewScreen
import com.bhoa.brawlcompetitive.ui.ProfileScreen
import com.bhoa.brawlcompetitive.data.Brawler
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


private fun sectionIcon(section: Section) = when (section) {
    Section.HOME -> Icons.Default.Home
    Section.META -> Icons.Default.BarChart
    Section.MAPS -> Icons.Default.Place
    Section.BRAWLERS -> Icons.Default.Person
    Section.COUNTERS -> Icons.Default.CompareArrows
    Section.PROFILE -> Icons.Default.AccountCircle
}

@Composable
fun BrawlCompetitiveApp() {
    var section by remember { mutableStateOf(Section.HOME) }
    var selectedBrawler by remember { mutableStateOf<Brawler?>(null) }
    var selectedMap by remember { mutableStateOf<BrawlMap?>(null) }
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
                if (selectedBrawler != null) {
                    BrawlerDetailScreen(selectedBrawler!!) { selectedBrawler = null }
                } else if (selectedMap != null) {
                    MapDetailScreen(selectedMap!!) { selectedMap = null }
                } else when (section) {
                    Section.HOME -> HomeScreen { section = it }
                    Section.BRAWLERS -> BrawlerCatalogScreen { selectedBrawler = it }
                    Section.MAPS -> MapCatalogScreen { selectedMap = it }
                    Section.META -> MetaOverviewScreen()
                    Section.COUNTERS -> CountersScreen()
                    Section.PROFILE -> ProfileScreen()
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

