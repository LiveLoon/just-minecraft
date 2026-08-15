package com.example.justmc.ui.pages

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.justmc.R
import com.example.justmc.data.StatType
import com.example.justmc.data.TopResponse
import com.example.justmc.network.ApiClient
import com.example.justmc.ui.components.TopEntryItem
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsPage(
    modifier: Modifier = Modifier
) {
    var statTypes by remember {
        mutableStateOf<List<StatType>>(emptyList())
    }

    var selectedKey by remember {
        mutableStateOf<String?>(null)
    }

    var topData by remember {
        mutableStateOf<TopResponse?>(null)
    }

    var isLoadingTypes by remember {
        mutableStateOf(true)
    }

    var isLoadingTop by remember {
        mutableStateOf(false)
    }

    var error by remember {
        mutableStateOf<String?>(null)
    }

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val coroutineScope = rememberCoroutineScope()


    // --------------------------------------------------
    // 加载统计类型
    // --------------------------------------------------

    LaunchedEffect(Unit) {
        try {
            val response = ApiClient.apiService.getStats()
            statTypes = response.stats
            if (statTypes.isNotEmpty()) {
                selectedKey = statTypes[0].key
            }
        } catch (e: Exception) {
            error = e.message
        } finally {
            isLoadingTypes = false
        }
    }


    // --------------------------------------------------
    // 根据统计类型加载排行榜
    // --------------------------------------------------

    LaunchedEffect(selectedKey) {
        val key = selectedKey ?: return@LaunchedEffect
        isLoadingTop = true
        error = null
        try {
            topData = ApiClient.apiService.getTop(key)
        } catch (e: Exception) {
            error = e.message
        } finally {
            isLoadingTop = false
        }
    }


    // --------------------------------------------------
    // 加载统计类型
    // --------------------------------------------------

    if (isLoadingTypes) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }


    // --------------------------------------------------
    // 加载统计类型失败
    // --------------------------------------------------

    if (error != null && statTypes.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Failed to load stats: $error",
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        return
    }


    // --------------------------------------------------
    // 没有统计类型
    // --------------------------------------------------

    if (statTypes.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No stats available",
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        return
    }


    // --------------------------------------------------
    // 当前选中的统计
    // --------------------------------------------------

    val selectedStat = statTypes.find {
        it.key == selectedKey
    }

    val selectedStatName = selectedStat?.let {
        stringResource(
            getStatStringRes(it.key)
        )
    } ?: stringResource(
        R.string.stats_unknown
    )


    // --------------------------------------------------
    // Drawer
    // --------------------------------------------------

    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface
            ) {
                Text(
                    text = stringResource(R.string.stats_category_list),
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Divider()
                LazyColumn {
                    itemsIndexed(
                        items = statTypes
                    ) { _, statType ->
                        val isSelected = statType.key == selectedKey
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    text = stringResource(
                                        getStatStringRes(statType.key)
                                    )
                                )
                            },
                            selected = isSelected,
                            onClick = {
                                selectedKey = statType.key
                                coroutineScope.launch {
                                    drawerState.close()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
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
                        .background(MaterialTheme.colorScheme.primary),
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
                            contentDescription = stringResource(R.string.open_stats_category),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Text(
                        text = "当前统计 "+selectedStatName,
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
                when {

                    // ------------------------------------------
                    // 加载排行榜
                    // ------------------------------------------
                    isLoadingTop -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    // ------------------------------------------
                    // 错误
                    // ------------------------------------------
                    error != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Error: $error",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // ------------------------------------------
                    // 没有数据
                    // ------------------------------------------
                    topData == null || topData!!.entries.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_stat_data),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // ------------------------------------------
                    // 排行榜
                    // ------------------------------------------
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            itemsIndexed(
                                items = topData!!.entries
                            ) { index, entry ->
                                TopEntryItem(
                                    entry = entry,
                                    rank = index + 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


// ==========================================================
// 统计 Key -> Android String Resource
// ==========================================================

@StringRes
fun getStatStringRes(key: String): Int {
    return when (key) {
        "playtime" -> R.string.stats_playtime
        "blocks_broken" -> R.string.stats_blocks_broken
        "blocks_placed" -> R.string.stats_blocks_placed
        "mob_kills" -> R.string.stats_mob_kills
        "player_kills" -> R.string.stats_player_kills
        "deaths" -> R.string.stats_deaths
        "damage_dealt" -> R.string.stats_damage_dealt
        "damage_taken" -> R.string.stats_damage_taken
        "distance_walked" -> R.string.stats_distance_walked
        "distance_sprinted" -> R.string.stats_distance_sprinted
        "distance_swam" -> R.string.stats_distance_swam
        "distance_flown" -> R.string.stats_distance_flown
        "distance_total" -> R.string.stats_distance_total
        "items_crafted" -> R.string.stats_items_crafted
        "items_dropped" -> R.string.stats_items_dropped
        "items_picked_up" -> R.string.stats_items_picked_up
        "chests_opened" -> R.string.stats_chests_opened
        "beds_slept_in" -> R.string.stats_beds_slept_in
        "xp_earned" -> R.string.stats_xp_earned
        "levels_gained" -> R.string.stats_levels_gained
        "money_earned" -> R.string.stats_money_earned
        "money_spent" -> R.string.stats_money_spent
        "money_balance" -> R.string.stats_money_balance
        "netherite_mined" -> R.string.stats_netherite_mined
        "diamond_mined" -> R.string.stats_diamond_mined
        "boss_kills" -> R.string.stats_boss_kills
        "emerald_mined" -> R.string.stats_emerald_mined
        "lapis_mined" -> R.string.stats_lapis_mined
        "redstone_mined" -> R.string.stats_redstone_mined
        "iron_mined" -> R.string.stats_iron_mined
        "gold_mined" -> R.string.stats_gold_mined
        "dragon_kills" -> R.string.stats_dragon_kills
        "wither_kills" -> R.string.stats_wither_kills
        "warden_kills" -> R.string.stats_warden_kills
        "breeze_kills" -> R.string.stats_breeze_kills
        "bogged_kills" -> R.string.stats_bogged_kills
        "nether_visits" -> R.string.stats_nether_visits
        "end_visits" -> R.string.stats_end_visits
        "netherite_ingots_crafted" -> R.string.stats_netherite_ingots_crafted
        "god_apples_crafted" -> R.string.stats_god_apples_crafted
        "maces_crafted" -> R.string.stats_maces_crafted
        "elytra_picked_up" -> R.string.stats_elytra_picked_up
        "trial_keys_picked_up" -> R.string.stats_trial_keys_picked_up
        "ominous_trial_keys_picked_up" -> R.string.stats_ominous_trial_keys_picked_up
        "beds_entered" -> R.string.stats_beds_entered
        "fish_caught" -> R.string.stats_fish_caught
        else -> R.string.stats_unknown
    }
}