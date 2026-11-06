package com.bhoa.brawlcompetitive.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bhoa.brawlcompetitive.data.BrawlMap
import com.bhoa.brawlcompetitive.data.MapRepository
import kotlinx.coroutines.launch

@Composable
fun MapCatalogScreen(onMapSelected: (BrawlMap) -> Unit) {
    val scope=rememberCoroutineScope()
    var maps by remember { mutableStateOf<List<BrawlMap>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    fun reload()=scope.launch { loading=true; maps=MapRepository.loadMaps(); loading=false }
    LaunchedEffect(Unit){reload()}
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mapas competitivos",style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Bold)
        Text("${maps.size} mapas disponibles")
        Spacer(Modifier.height(12.dp))
        if(loading) Box(Modifier.fillMaxSize(),contentAlignment=Alignment.Center){CircularProgressIndicator()}
        else LazyVerticalGrid(columns=GridCells.Adaptive(160.dp),verticalArrangement=Arrangement.spacedBy(12.dp),horizontalArrangement=Arrangement.spacedBy(12.dp)){
            items(maps,key={it.id}){ map ->
                ElevatedCard(Modifier.fillMaxWidth().clickable{onMapSelected(map)}) {
                    Column(Modifier.padding(10.dp)) {
                        if(!map.imageUrl.isNullOrBlank()) AsyncImage(map.imageUrl,map.name,Modifier.fillMaxWidth().height(110.dp),contentScale=ContentScale.Crop)
                        else Box(Modifier.fillMaxWidth().height(110.dp),contentAlignment=Alignment.Center){Text("Sin imagen disponible")}
                        Text(map.name,fontWeight=FontWeight.Bold)
                        map.mode?.let{Text(it,style=MaterialTheme.typography.bodySmall)}
                    }
                }
            }
        }
    }
}
