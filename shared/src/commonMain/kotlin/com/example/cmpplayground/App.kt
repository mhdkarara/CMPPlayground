package com.example.cmpplayground

import androidx.compose.runtime.Composable
import com.example.cmpplayground.core.designsystem.theme.AppTheme
import com.example.cmpplayground.core.navigation.AppNavigation

@Composable
fun App() {
    AppTheme { AppNavigation() }
}

@Composable
expect fun CatalogBackHandler(enabled: Boolean, onBack: () -> Unit)
