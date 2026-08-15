package com.example.justmc.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.justmc.data.Player

@Composable
fun PlayerItem(player: Player) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Name: ${player.name ?: "Unknown"}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(text = "UUID: ${player.uuid}", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = "Gamemode: ${player.gamemode}", fontSize = 14.sp)
            Text(
                text = "Status: ${if (player.isOnline) "Online" else "Offline"}",
                fontSize = 14.sp,
                color = if (player.isOnline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }
    }
}