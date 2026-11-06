package com.bhoa.brawlcompetitive.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bhoa.brawlcompetitive.data.BrawlMap

@Composable
fun MapDetailScreen(map:BrawlMap,onBack:()->Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        TextButton(onClick=onBack){Text("← Volver a mapas")}
        Text(map.name,style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Bold)
        if(!map.imageUrl.isNullOrBlank()) AsyncImage(map.imageUrl,map.name,Modifier.fillMaxWidth().height(260.dp),contentScale=ContentScale.Fit)
        else ElevatedCard { Text("La fuente de datos todavía no proporciona una imagen para este mapa.",Modifier.padding(16.dp)) }
        map.mode?.let { Text("Modo: $it",style=MaterialTheme.typography.titleMedium) }
        map.environment?.let { Text("Entorno: $it") }
    }
}
