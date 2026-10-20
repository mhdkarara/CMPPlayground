package com.example.cmpplayground.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// Swift's protocol-like boundary: each platform provides its own actual implementation.
@Composable
expect fun NativeSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier = Modifier)

expect val nativeSwitchName: String
