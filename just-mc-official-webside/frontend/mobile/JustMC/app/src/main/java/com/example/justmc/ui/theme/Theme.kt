package com.example.justmc.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


// 浅色主题（Minecraft 草原风格）
private val LightColorScheme = lightColorScheme(
    primary = GrassGreen,
    secondary = WoodBrown,
    tertiary = GoldYellow,
    background = SandBeige,
    surface = SandBeige,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

// 深色主题（Minecraft 地下/下界风格）
private val DarkColorScheme = darkColorScheme(
    primary = DeepBrown,
    secondary = StoneBlueGray,
    tertiary = GlowstoneGold,
    background = ObsidianBlack,
    surface = ObsidianBlack,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onBackground = Color(0xFFE0E0E0),
    onSurface = Color(0xFFE0E0E0)
)

@Composable
fun JustMCTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // ⚠️ 将动态颜色默认改为 false，固定使用我们的配色
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // 如果 dynamicColor 为 true 且系统支持，才启用动态（但我们现在默认 false）
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}