package com.example.cmpplayground.platform

import androidx.compose.runtime.Composable

@Composable
expect fun rememberCopyToClipboard(): (String) -> Unit
