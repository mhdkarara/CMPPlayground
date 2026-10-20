package com.example.cmpplayground.feature.learn.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.cmpplayground.platform.NativeSwitch
import com.example.cmpplayground.platform.nativeSwitchName
import com.example.cmpplayground.platform.getPlatform

@Composable
fun PlatformComparisonScreen(categoryTitle: String = "", description: String = "") {
    var enabled by rememberSaveable { mutableStateOf(false) }
    var changes by rememberSaveable { mutableIntStateOf(0) }
    var lastControl by rememberSaveable { mutableStateOf("None") }
    var text by rememberSaveable { mutableStateOf("") }
    var keyboard by rememberSaveable { mutableIntStateOf(0) }
    var hapticRequests by rememberSaveable { mutableIntStateOf(0) }
    val haptics = LocalHapticFeedback.current
    val keyboardTypes = listOf(KeyboardType.Text, KeyboardType.Email, KeyboardType.Number)

    Column(Modifier.fillMaxSize().imePadding().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (categoryTitle.isNotEmpty()) {
            Text(categoryTitle, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Text("Shared vs platform code", style = MaterialTheme.typography.headlineSmall)
        }
        Text(description.ifEmpty { "Compare shared Compose UI with native controls and platform information." },
            style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(getPlatform().name, style = MaterialTheme.typography.titleMedium)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Compose Switch")
            Switch(enabled, { enabled = it; changes++; lastControl = "Compose" })
        }
        HorizontalDivider()
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(nativeSwitchName, modifier = Modifier.weight(1f))
            NativeSwitch(enabled, { enabled = it; changes++; lastControl = nativeSwitchName },
                Modifier.width(64.dp).height(48.dp))
        }
        Text("State: $enabled  |  Changes: $changes")
        Text("Last input: $lastControl")
        HorizontalDivider()
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
            listOf("Text", "Email", "Number").forEachIndexed { index, title ->
                SegmentedButton(selected = keyboard == index, onClick = { keyboard = index },
                    shape = SegmentedButtonDefaults.itemShape(index, 3)) { Text(title) }
            }
        }
        OutlinedTextField(text, { text = it }, modifier = Modifier.fillMaxWidth(),
            label = { Text("Keyboard input") }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardTypes[keyboard]))
        HorizontalDivider()
        Button({ haptics.performHapticFeedback(HapticFeedbackType.LongPress); hapticRequests++ }) {
            Text("Haptic feedback")
        }
        Text("Haptic requests: $hapticRequests")
    }
}
