package com.bhoa.brawlcompetitive.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bhoa.brawlcompetitive.data.Brawler
import com.bhoa.brawlcompetitive.data.VisualAsset

@Composable
fun BrawlerDetailScreen(brawler:Brawler,onBack:()->Unit) {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        item {
            Row { TextButton(onClick=onBack){Text("← Volver")}; Text(brawler.name,style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Bold) }
            if(!brawler.imageUrl.isNullOrBlank()) AsyncImage(brawler.imageUrl,brawler.name,Modifier.fillMaxWidth().height(220.dp),contentScale=ContentScale.Fit)
            brawler.description?.let{Text(it)}
        }
        assetSection("Estelares",brawler.starPowers)
        assetSection("Gadgets",brawler.gadgets)
        assetSection("Refuerzos",brawler.gears)
        if(brawler.buffies.isNotEmpty()) assetSection("Buffies",brawler.buffies)
    }
}
private fun androidx.compose.foundation.lazy.LazyListScope.assetSection(title:String,assets:List<VisualAsset>) {
    if(assets.isEmpty()) return
    item { Text(title,style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Bold) }
    items(assets,key={it.id}) { asset ->
        ElevatedCard(Modifier.fillMaxWidth()) {
            Row(Modifier.padding(12.dp)) {
                if(!asset.imageUrl.isNullOrBlank()) AsyncImage(asset.imageUrl,asset.name,Modifier.size(72.dp),contentScale=ContentScale.Fit)
                Spacer(Modifier.width(12.dp))
                Column { Text(asset.name,fontWeight=FontWeight.Bold); asset.description?.let{Text(it,style=MaterialTheme.typography.bodySmall)} }
            }
        }
    }
}
