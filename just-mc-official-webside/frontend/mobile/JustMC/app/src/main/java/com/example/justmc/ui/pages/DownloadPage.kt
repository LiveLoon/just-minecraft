package com.example.justmc.ui.pages

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.justmc.data.BackupFile
import com.example.justmc.network.ApiClient
import com.example.justmc.ui.theme.Redstone
import com.example.justmc.ui.theme.SkyBlue
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

enum class SortType {
    NAME_ASC, NAME_DESC, SIZE_ASC, SIZE_DESC, TIME_ASC, TIME_DESC
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadPage(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var backupFiles by remember { mutableStateOf<List<BackupFile>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var sortType by rememberSaveable { mutableStateOf(SortType.TIME_DESC) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            backupFiles = ApiClient.apiService.getBackupList()
        } catch (e: Exception) {
            error = e.message ?: "加载存档列表失败"
        } finally {
            isLoading = false
        }
    }

    fun getLatestFile(files: List<BackupFile>): BackupFile? {
        return files.maxByOrNull { it.mtime.toLongOrNull() ?: 0L }
    }

    fun sortFiles(files: List<BackupFile>, type: SortType): List<BackupFile> {
        return when (type) {
            SortType.NAME_ASC -> files.sortedBy { it.name }
            SortType.NAME_DESC -> files.sortedByDescending { it.name }
            SortType.SIZE_ASC -> files.sortedBy { it.size }
            SortType.SIZE_DESC -> files.sortedByDescending { it.size }
            SortType.TIME_ASC -> files.sortedBy { it.mtime.toLongOrNull() ?: 0L }
            SortType.TIME_DESC -> files.sortedByDescending { it.mtime.toLongOrNull() ?: 0L }
        }
    }

    fun buildDisplayList(files: List<BackupFile>, type: SortType): Pair<BackupFile?, List<BackupFile>> {
        if (files.isEmpty()) return null to emptyList()
        val latest = getLatestFile(files)
        val rest = if (latest != null) files.filter { it != latest } else files
        val sortedRest = sortFiles(rest, type)
        return latest to sortedRest
    }

    fun formatTime(timestamp: String): String {
        return try {
            val millis = timestamp.toLong()
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            sdf.format(Date(millis))
        } catch (e: Exception) {
            timestamp
        }
    }

    fun formatSize(size: Long): String {
        return when {
            size < 1024 -> "$size B"
            size < 1024 * 1024 -> String.format("%.2f KB", size / 1024.0)
            size < 1024 * 1024 * 1024 -> String.format("%.2f MB", size / (1024.0 * 1024))
            else -> String.format("%.2f GB", size / (1024.0 * 1024 * 1024))
        }
    }

    fun copyDownloadLink(fileName: String) {
        try {
            val url = "${ApiClient.BASE_URL}backup/download/$fileName"
            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("下载链接", url)
            clipboardManager.setPrimaryClip(clip)
            Toast.makeText(context, "下载链接已复制: $url", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, "复制链接失败: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun getSortDisplay(type: SortType): String {
        return when (type) {
            SortType.NAME_ASC -> "名称 A-Z"
            SortType.NAME_DESC -> "名称 Z-A"
            SortType.SIZE_ASC -> "大小 小→大"
            SortType.SIZE_DESC -> "大小 大→小"
            SortType.TIME_ASC -> "时间 旧→新"
            SortType.TIME_DESC -> "时间 新→旧"
        }
    }

    if (isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    if (error != null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                "Error: $error",
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        return
    }

    val (latest, sortedRest) = buildDisplayList(backupFiles, sortType)

    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface
            ) {
                Text(
                    text = "排序方式",
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Divider()
                SortType.values().forEach { type ->
                    val isSelected = sortType == type
                    NavigationDrawerItem(
                        label = { Text(getSortDisplay(type)) },
                        selected = isSelected,
                        onClick = {
                            sortType = type
                            coroutineScope.launch { drawerState.close() }
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
                            coroutineScope.launch { drawerState.open() }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "排序方式",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Text(
                        text = "存档下载 (${getSortDisplay(sortType)})",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                when {
                    backupFiles.isEmpty() -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                "没有存档文件",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (latest != null) {
                                item {
                                    BackupFileItem(
                                        file = latest,
                                        isLatest = true,
                                        onCopyLink = ::copyDownloadLink,
                                        formatSize = ::formatSize,
                                        formatTime = ::formatTime
                                    )
                                }
                            }
                            items(sortedRest) { file ->
                                BackupFileItem(
                                    file = file,
                                    isLatest = false,
                                    onCopyLink = ::copyDownloadLink,
                                    formatSize = ::formatSize,
                                    formatTime = ::formatTime
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BackupFileItem(
    file: BackupFile,
    isLatest: Boolean,
    onCopyLink: (String) -> Unit,
    formatSize: (Long) -> String,
    formatTime: (String) -> String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isLatest) 4.dp else 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isLatest)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = file.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (isLatest) {
                        Surface(
                            color = Redstone,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = "最新",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Text(
                    text = "大小: ${formatSize(file.size)}  |  修改: ${formatTime(file.mtime)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Button(
                onClick = { onCopyLink(file.name) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = SkyBlue
                )
            ) {
                Text(
                    text = "复制链接",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}