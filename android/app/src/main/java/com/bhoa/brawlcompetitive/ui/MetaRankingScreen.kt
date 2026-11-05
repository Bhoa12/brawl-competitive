package com.bhoa.brawlcompetitive.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bhoa.brawlcompetitive.data.BrawlerCatalog
import com.bhoa.brawlcompetitive.network.AnalyticsResponse

@Composable
fun MetaRankingScreen(
    response: AnalyticsResponse,
    onBrawlerSelected: (String) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Ranking del meta", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Muestra: " + response.sampleSize + " · Actualizado: " + response.updatedAt)
            Spacer(Modifier.height(8.dp))
        }

        itemsIndexed(response.rows) { index, row ->
            val brawler = BrawlerCatalog.find(row.brawlerId) ?: BrawlerCatalog.fallback(row.brawlerId)
            ElevatedCard(
                modifier = Modifier.fillMaxWidth().clickable { onBrawlerSelected(row.brawlerId) }
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("#" + (index + 1), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(12.dp))
                    if (brawler.imageUrl.isNotBlank()) {
                        AsyncImage(
                            model = brawler.imageUrl,
                            contentDescription = brawler.name,
                            modifier = Modifier.size(72.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(brawler.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text("Pick rate: " + row.pickRate + "%")
                        Text("Win rate: " + row.winRate + "%")
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Picks " + row.picks, style = MaterialTheme.typography.labelSmall)
                        Text("Wins " + row.wins, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}
