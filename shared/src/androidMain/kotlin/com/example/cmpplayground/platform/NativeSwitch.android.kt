package com.example.cmpplayground.platform

import android.widget.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

actual val nativeSwitchName: String = "android.widget.Switch"

@Composable
actual fun NativeSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier) {
    AndroidView(
        factory = { context -> Switch(context).apply { contentDescription = "Native switch" } },
        modifier = modifier,
        update = { control ->
            // Detach the callback so synchronizing shared state is not treated as user input.
            control.setOnCheckedChangeListener(null)
            control.isChecked = checked
            control.setOnCheckedChangeListener { _, value -> onCheckedChange(value) }
        },
    )
}
