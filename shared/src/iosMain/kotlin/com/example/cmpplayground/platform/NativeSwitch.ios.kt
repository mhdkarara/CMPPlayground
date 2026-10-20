package com.example.cmpplayground.platform

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import kotlinx.cinterop.ExperimentalForeignApi
import platform.darwin.NSObject
import platform.UIKit.UISwitch
import platform.UIKit.UIControlEventValueChanged
import platform.UIKit.accessibilityLabel
import platform.objc.sel_registerName
import kotlinx.cinterop.ObjCAction

actual val nativeSwitchName: String = "UISwitch"

@OptIn(kotlinx.cinterop.BetaInteropApi::class)
private class SwitchTarget(var onChange: (Boolean) -> Unit) : NSObject() {
    @ObjCAction
    fun changed(sender: UISwitch) { onChange(sender.on) }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun NativeSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier) {
    val callback by rememberUpdatedState(onCheckedChange)
    val target = remember { SwitchTarget { callback(it) } }
    UIKitView(
        factory = {
            UISwitch().apply {
                accessibilityLabel = "Native switch"
                addTarget(target, sel_registerName("changed:"), UIControlEventValueChanged)
            }
        },
        modifier = modifier,
        properties = UIKitInteropProperties(isNativeAccessibilityEnabled = true),
        update = { it.setOn(checked, animated = true) },
    )
}
