package com.example.cmpplayground.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val colors = if (isSystemInDarkTheme()) {
        darkColorScheme(primary = Color(0xFF9CCAFF), secondary = Color(0xFF84D7C8))
    } else {
        lightColorScheme(
            primary = Color(0xFF245B8F), onPrimary = Color.White,
            secondary = Color(0xFF176B5B), background = Color(0xFFF8FAFC), surface = Color(0xFFF8FAFC),
        )
    }
    MaterialTheme(
        colorScheme = colors,
        typography = Typography(),
        shapes = Shapes(
            extraSmall = RoundedCornerShape(4.dp),
            small = RoundedCornerShape(8.dp),
            medium = RoundedCornerShape(12.dp),
            large = RoundedCornerShape(16.dp),
            extraLarge = RoundedCornerShape(20.dp),
        ),
        content = content,
    )
}
