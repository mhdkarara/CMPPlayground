package com.example.cmpplayground.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.UIPasteboard

@Composable
actual fun rememberCopyToClipboard(): (String) -> Unit = remember {
    { text -> UIPasteboard.generalPasteboard.string = text }
}
