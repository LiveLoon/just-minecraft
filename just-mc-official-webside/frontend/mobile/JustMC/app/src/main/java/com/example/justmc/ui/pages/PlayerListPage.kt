package com.example.justmc.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.justmc.data.Player
import com.example.justmc.network.ApiClient
import kotlinx.coroutines.launch
import java.io.IOException

enum class FilterType {
    ALL,
    ONLINE,
    OFFLINE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerListPage(
    modifier: Modifier = Modifier,
    onDrawerStateChanged: (Boolean) -> Unit = {}
) {
    var allPlayers by remember { mutableStateOf<List<Player>>(emptyList()) }
    var filteredPlayers by remember { mutableStateOf<List<Player>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var filterType by remember { mutableStateOf(FilterType.ALL) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(drawerState.currentValue) {
        onDrawerStateChanged(
            drawerState.currentValue == DrawerValue.Open
        )
    }

    LaunchedEffect(Unit) {
        try {
            val all = ApiClient.apiService.getPlayerList()

            allPlayers = all.filter {
                it.name != null
            }

            filteredPlayers = allPlayers
        } catch (e: IOException) {
            error = "Network error: ${e.message}"
        } catch (e: Exception) {
            error = "Error: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    LaunchedEffect(filterType, allPlayers) {
        filteredPlayers = when (filterType) {
            FilterType.ALL -> allPlayers

            FilterType.ONLINE ->
                allPlayers.filter { it.isOnline }

            FilterType.OFFLINE ->
                allPlayers.filter { !it.isOnline }
        }
    }

    val sortedPlayers = remember(filteredPlayers) {
        filteredPlayers.sortedWith(
            compareBy<Player> { !it.isOnline }
                .thenBy { it.name ?: "" }
        )
    }

    if (isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        return
    }

    if (error != null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = error ?: "未知错误",
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        return
    }

    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        gesturesEnabled = false,

        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface
            ) {
                Text(
                    text = "筛选玩家",
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                HorizontalDivider()

                listOf(
                    FilterType.ONLINE to "在线",
                    FilterType.OFFLINE to "离线"
                ).forEach { (type, label) ->

                    val isSelected = filterType == type

                    NavigationDrawerItem(
                        label = {
                            Text(label)
                        },

                        selected = isSelected,

                        onClick = {
                            filterType = type

                            coroutineScope.launch {
                                drawerState.close()
                            }
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 8.dp,
                                vertical = 4.dp
                            ),

                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor =
                                MaterialTheme.colorScheme.primaryContainer,

                            selectedTextColor =
                                MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }

                HorizontalDivider()

                NavigationDrawerItem(
                    label = {
                        Text("全部玩家")
                    },

                    selected = filterType == FilterType.ALL,

                    onClick = {
                        filterType = FilterType.ALL

                        coroutineScope.launch {
                            drawerState.close()
                        }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 8.dp,
                            vertical = 4.dp
                        )
                )
            }
        }
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,

            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(
                            MaterialTheme.colorScheme.primary
                        ),

                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "筛选玩家",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Text(
                        text = when (filterType) {
                            FilterType.ALL -> "全部玩家"
                            FilterType.ONLINE -> "在线玩家"
                            FilterType.OFFLINE -> "离线玩家"
                        },

                        color = MaterialTheme.colorScheme.onPrimary,

                        fontSize = 18.sp,

                        fontWeight = FontWeight.Medium
                    )
                }
            }
        ) { innerPadding ->

            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {

                if (sortedPlayers.isEmpty()) {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "没有符合条件的玩家",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                } else {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),

                        contentPadding = PaddingValues(
                            top = 12.dp,
                            bottom = 16.dp
                        ),

                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(
                            items = sortedPlayers,
                            key = { player ->
                                player.uuid ?: player.name ?: ""
                            }
                        ) { player ->

                            PlayerItem(player)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlayerItem(player: Player) {

    val isBedrock = player.name
        ?.startsWith(".") == true

    val displayName = player.name
        ?.removePrefix(".")
        ?.takeIf { it.isNotEmpty() }
        ?: "Unknown"

    val borderColor =
        if (player.isOnline) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)
        } else {
            MaterialTheme.colorScheme.outlineVariant
        }

    val statusColor =
        if (player.isOnline) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outline
        }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(14.dp)
            ),

        shape = RoundedCornerShape(14.dp),

        elevation = CardDefaults.cardElevation(
            defaultElevation = if (player.isOnline) 3.dp else 1.dp
        ),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            // 左侧在线状态条
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(132.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 14.dp,
                            bottomStart = 14.dp
                        )
                    )
                    .background(statusColor)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        start = 14.dp,
                        top = 14.dp,
                        end = 14.dp,
                        bottom = 14.dp
                    ),

                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {

                // 玩家名称
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = displayName,

                        style = MaterialTheme.typography.titleMedium,

                        fontWeight = FontWeight.Bold,

                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (isBedrock) {

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Surface(
                            shape = RoundedCornerShape(6.dp),

                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Text(
                                text = "基岩版",

                                modifier = Modifier.padding(
                                    horizontal = 7.dp,
                                    vertical = 3.dp
                                ),

                                fontSize = 11.sp,

                                fontWeight = FontWeight.Medium,

                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.weight(1f)
                    )

                    // 在线 / 离线
                    Surface(
                        shape = RoundedCornerShape(20.dp),

                        color = if (player.isOnline) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    ) {
                        Text(
                            text = if (player.isOnline) {
                                "在线"
                            } else {
                                "离线"
                            },

                            modifier = Modifier.padding(
                                horizontal = 9.dp,
                                vertical = 4.dp
                            ),

                            fontSize = 11.sp,

                            fontWeight = FontWeight.Medium,

                            color = if (player.isOnline) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(
                        alpha = 0.5f
                    )
                )

                // UUID
                PlayerInfoRow(
                    label = "UUID",
                    value = player.uuid ?: "未知"
                )

                // 游戏模式
                PlayerInfoRow(
                    label = "游戏模式",
                    value = translateGamemode(player.gamemode)
                )
            }
        }
    }
}

@Composable
private fun PlayerInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = label,

            modifier = Modifier.width(72.dp),

            fontSize = 12.sp,

            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,

            fontSize = 13.sp,

            color = MaterialTheme.colorScheme.onSurface,

            fontWeight = FontWeight.Medium
        )
    }
}

private fun translateGamemode(gamemode: String?): String {
    return when (gamemode?.lowercase()) {

        "survival" -> "生存"

        "creative" -> "创造"

        "adventure" -> "冒险"

        "spectator" -> "旁观"

        "hardcore" -> "极限"

        else -> gamemode
            ?.replaceFirstChar {
                if (it.isLowerCase()) {
                    it.titlecase()
                } else {
                    it.toString()
                }
            }
            ?: "未知"
    }
}