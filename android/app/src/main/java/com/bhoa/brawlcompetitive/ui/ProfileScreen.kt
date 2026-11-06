package com.bhoa.brawlcompetitive.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bhoa.brawlcompetitive.data.PlayerProfile
import com.bhoa.brawlcompetitive.data.PlayerRepository
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen() {
    var tag by remember { mutableStateOf("") }
    var profile by remember { mutableStateOf<PlayerProfile?>(null) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    fun search() = scope.launch {
        loading = true
        error = null
        profile = null
        PlayerRepository.load(tag).onSuccess { profile = it }.onFailure { error = it.message ?: "No se pudo cargar el perfil" }
        loading = false
    }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Perfil de jugador", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Busca un jugador usando su tag. Las credenciales oficiales permanecen protegidas en el servidor.")
        OutlinedTextField(value = tag, onValueChange = { tag = it }, label = { Text("Tag del jugador") }, placeholder = { Text("#ABC123") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        Button(onClick = { search() }, enabled = !loading && tag.trim().isNotBlank(), modifier = Modifier.fillMaxWidth()) {
            Text(if (loading) "Buscando…" else "Buscar jugador")
        }
        if (loading) Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        profile?.let { p ->
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(p.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(p.tag)
                    HorizontalDivider()
                    Text("Trofeos: " + p.trophies)
                    Text("Récord de trofeos: " + p.highestTrophies)
                    Text("Nivel de experiencia: " + p.experienceLevel)
                }
            }
        }
    }
}
