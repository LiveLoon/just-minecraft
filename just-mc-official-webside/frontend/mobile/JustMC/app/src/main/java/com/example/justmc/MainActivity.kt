package com.example.justmc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddChart
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.justmc.ui.pages.AboutPage
import com.example.justmc.ui.pages.DownloadPage
import com.example.justmc.ui.pages.PlayerListPage
import com.example.justmc.ui.pages.StatsPage
import com.example.justmc.ui.theme.GrassGreen
import com.example.justmc.ui.theme.JustMCTheme
import kotlinx.coroutines.launch

enum class AppDestinations(
    val label: String,
    val icon: ImageVector
) {
    HOME("玩家信息", Icons.Default.Apps),
    FAVORITES("统计数据", Icons.Default.AddChart),
    DOWNLOAD("存档下载", Icons.Default.FileDownload),
    ABOUT("关于我们", Icons.Default.Info)
}

@Composable
fun JustMCApp() {

    var currentDestination by rememberSaveable {
        mutableStateOf(AppDestinations.HOME)
    }

    // 创建 Pager 状态
    val pagerState = rememberPagerState(pageCount = { AppDestinations.entries.size })
    val coroutineScope = rememberCoroutineScope()

// 监听 Pager 滑动，同步 currentDestination
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { page ->
                currentDestination = AppDestinations.entries[page]
            }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        // 页面背景
        containerColor = Color(0xFFF5F0E1),
        // 底部导航栏
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth().height(65.dp)
                    .background(Color(0xFFF5F0E1))
                    .navigationBarsPadding()
            ) {
                NavigationBar(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    windowInsets = WindowInsets(0, 0, 0, 0)
                ) {
                    AppDestinations.entries.forEachIndexed { index, destination ->
                        val isSelected = destination == currentDestination

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                                currentDestination = destination
                            },
                            icon = {
                                Icon(
                                    imageVector = destination.icon,
                                    contentDescription = destination.label
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                                indicatorColor = MaterialTheme.colorScheme.secondary,
                                unselectedIconColor =
                                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f),
                                unselectedTextColor =
                                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        // 内容区域：HorizontalPager 占据剩余空间
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), // 避开底部导航栏
        ) { page ->
            when (AppDestinations.entries[page]) {
                AppDestinations.HOME -> PlayerListPage(Modifier.fillMaxSize())
                AppDestinations.FAVORITES -> StatsPage(Modifier.fillMaxSize())
                AppDestinations.DOWNLOAD -> DownloadPage(Modifier.fillMaxSize())
                AppDestinations.ABOUT -> AboutPage(Modifier.fillMaxSize())
            }
        }
    }
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            JustMCTheme {
                JustMCApp()
            }
        }
    }
}