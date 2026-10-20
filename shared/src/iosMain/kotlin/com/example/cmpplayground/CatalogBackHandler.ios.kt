package com.example.cmpplayground

import androidx.compose.runtime.Composable

// iOS uses the shared navigation bar; Android additionally supports system back.
@Composable
actual fun CatalogBackHandler(enabled: Boolean, onBack: () -> Unit) = Unit
