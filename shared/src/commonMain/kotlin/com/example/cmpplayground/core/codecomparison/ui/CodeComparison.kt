package com.example.cmpplayground.core.codecomparison.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.cmpplayground.core.designsystem.tokens.AppSpacing
import com.example.cmpplayground.core.codecomparison.model.CodeExample
import com.example.cmpplayground.platform.rememberCopyToClipboard
import kotlinx.coroutines.launch

enum class CodeMode { SWIFTUI, COMPOSE, COMPARE }

@Composable
fun CodeComparison(example: CodeExample, modifier: Modifier = Modifier) {
    var mode by remember(example.title) { mutableStateOf(CodeMode.COMPARE) }
    Column(modifier, verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
        Text("Code comparison", style = MaterialTheme.typography.titleLarge)
        Text(example.description, style = MaterialTheme.typography.bodyMedium)
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
            CodeMode.entries.forEachIndexed { index, item ->
                SegmentedButton(selected = mode == item, onClick = { mode = item },
                    shape = SegmentedButtonDefaults.itemShape(index, CodeMode.entries.size)) {
                    Text(when (item) { CodeMode.SWIFTUI -> "SwiftUI"; CodeMode.COMPOSE -> "Compose"; CodeMode.COMPARE -> "Compare" })
                }
            }
        }
        when (mode) {
            CodeMode.SWIFTUI -> CodePanel("SwiftUI", example.swiftCode)
            CodeMode.COMPOSE -> CodePanel("Kotlin · Compose", example.kotlinCode)
            CodeMode.COMPARE -> BoxWithConstraints(Modifier.fillMaxWidth()) {
                if (maxWidth < AppSpacing.tabletBreakpoint) {
                    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                        CodePanel("SwiftUI", example.swiftCode, Modifier.fillMaxWidth())
                        CodePanel("Kotlin · Compose", example.kotlinCode, Modifier.fillMaxWidth())
                    }
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                        CodePanel("SwiftUI", example.swiftCode, Modifier.weight(1f))
                        CodePanel("Kotlin · Compose", example.kotlinCode, Modifier.weight(1f))
                    }
                }
            }
        }
        Text(example.explanation, style = MaterialTheme.typography.bodyMedium)
        example.keyDifferences.forEach { Text("• $it", style = MaterialTheme.typography.bodyMedium) }
    }
}

@Composable
private fun CodePanel(language: String, code: String, modifier: Modifier = Modifier) {
    val copyToClipboard = rememberCopyToClipboard()
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Card(modifier) {
        Column(Modifier.fillMaxWidth().padding(AppSpacing.md), verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(language, style = MaterialTheme.typography.labelLarge)
                TextButton(onClick = {
                    copyToClipboard(code)
                    scope.launch { snackbar.showSnackbar("$language copied") }
                }) { Text("Copy") }
            }
            SelectionContainer {
                Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
                    Text(code, fontFamily = FontFamily.Monospace, style = MaterialTheme.typography.bodySmall)
                }
            }
            SnackbarHost(snackbar)
        }
    }
}
