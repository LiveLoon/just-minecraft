package com.example.justmc.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.justmc.data.TopEntry

@Composable
fun TopEntryItem(
    entry: TopEntry,
    rank: Int
) {
    // 金、银、铜
    val gold = Color(0xFFFFD700)
    val silver = Color(0xFFC0C0C0)
    val bronze = Color(0xFFCD7F32)

    // 背景颜色
    val backgroundColor = when (rank) {
        1 -> gold.copy(alpha = 0.15f)
        2 -> silver.copy(alpha = 0.15f)
        3 -> bronze.copy(alpha = 0.15f)
        else -> MaterialTheme.colorScheme.surface
    }

    // 边框颜色
    val borderColor = when (rank) {
        1 -> gold
        2 -> silver
        3 -> bronze
        else -> Color.Transparent
    }

    // 图标颜色
    val iconColor = when (rank) {
        1 -> gold
        2 -> silver
        3 -> bronze
        else -> null
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 6.dp
            ),

        // 只有前三名有边框
        border = if (rank <= 3) {
            BorderStroke(
                width = 2.dp,
                color = borderColor
            )
        } else {
            null
        },

        elevation = CardDefaults.cardElevation(
            defaultElevation = if (rank <= 3) {
                4.dp
            } else {
                1.dp
            }
        ),

        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // =========================
            // 排名
            // =========================

            Text(
                text = "#$rank",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.width(48.dp),
                color = when (rank) {
                    1 -> gold
                    2 -> Color(0xFF808080)
                    3 -> bronze
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )

            // =========================
            // 前三名奖杯
            // =========================

            if (iconColor != null) {

                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Rank $rank",
                    tint = iconColor,
                    modifier = Modifier.size(28.dp)
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )
            }

            // =========================
            // 玩家信息
            // =========================

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = entry.name ?: "Unknown",
                    fontWeight = if (rank <= 3) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Medium
                    },
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = shortenUuid(entry.uuid),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // =========================
            // 数据
            // =========================

            Text(
                text = entry.value_human,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = when (rank) {
                    1 -> gold
                    2 -> Color(0xFF808080)
                    3 -> bronze
                    else -> MaterialTheme.colorScheme.primary
                }
            )
        }
    }
}

private fun shortenUuid(uuid: String): String {
    return if (uuid.length > 14) {
        "${uuid.take(8)}...${uuid.takeLast(6)}"
    } else {
        uuid
    }
}