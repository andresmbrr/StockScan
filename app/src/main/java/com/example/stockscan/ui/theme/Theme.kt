package com.example.stockscan.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColoresStockScan = lightColorScheme(
    primary = Color(0xFF006F60),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD9F1EA),
    onPrimaryContainer = Color(0xFF083E36),
    background = Color(0xFFF7FAF9),
    onBackground = Color(0xFF15332D),
    surface = Color.White,
    onSurface = Color(0xFF15332D)
)

@Composable
fun StockScanTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = ColoresStockScan, content = content)
}
