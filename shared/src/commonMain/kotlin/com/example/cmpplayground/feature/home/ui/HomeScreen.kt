package com.example.cmpplayground.feature.home.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cmpplayground.core.designsystem.tokens.AppSpacing

@Composable
fun HomeScreen(
    onCatalog: () -> Unit,
    onLearn: () -> Unit,
    onCounter: () -> Unit,
    onCalculator: () -> Unit,
    onFeatured: (String, String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = AppSpacing.screen, vertical = AppSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.xl),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
                Text("CMP Playground", style = MaterialTheme.typography.headlineMedium)
                Text("Learn Compose through the eyes of a SwiftUI developer.",
                    style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.md)) {
                SectionHeading("Start exploring")
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)) {
                    Shortcut("Components", "Browse 43 examples", Icons.Default.Search, onCatalog, Modifier.weight(1f))
                    Shortcut("SwiftUI → Compose", "Learn the mapping", Icons.Default.Info, onLearn, Modifier.weight(1f))
                }
            }
        }
        item {
            Column {
                SectionHeading("Featured components", "A few useful places to begin")
                listOf(
                Featured("Button", "Actions, callbacks, and visual styles", "essentials", "save-actions"),
                Featured("Text", "Typography and selectable content", "essentials", "reading-type"),
                Featured("Row & Column", "Arrange content in two directions", "layout", "gear-row"),
                Featured("Text field", "Editable state and user input", "input", "trip-notes"),
                ).forEach { item ->
                    ListItem(
                        headlineContent = { Text(item.title) },
                        supportingContent = { Text(item.description) },
                        trailingContent = { Text("View", color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge) },
                        modifier = Modifier.fillMaxWidth().clickable { onFeatured(item.categoryId, item.demoId) },
                    )
                    HorizontalDivider()
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.md)) {
                SectionHeading("Interactive playground", "Small apps with real state and behavior")
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)) {
                    FeatureShortcut("Counter", "Beginner · StateFlow and MVVM", Icons.Default.Add, onCounter, Modifier.weight(1f))
                    FeatureShortcut("Calculator", "Intermediate · UI state and actions", Icons.Default.Settings, onCalculator, Modifier.weight(1f))
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.md)) {
                SectionHeading("Learning journey", "Pick a concept and connect it to what you know")
                LearningLine("State management", "Compare @State with Compose state and StateFlow.")
                LearningLine("MVVM", "Follow a user action from screen to ViewModel and back.")
                LearningLine("SwiftUI vs Compose", "Compare the APIs, then try each interaction.")
                TextButton(onClick = onLearn) { Text("Browse learning topics") }
            }
        }
    }
}

@Composable
private fun SectionHeading(title: String, subtitle: String? = null) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge)
        subtitle?.let { Text(it, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
    }
}

@Composable
private fun Shortcut(title: String, detail: String, icon: androidx.compose.ui.graphics.vector.ImageVector,
                     onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedCard(onClick = onClick, modifier = modifier.heightIn(min = 112.dp)) {
        Column(Modifier.fillMaxWidth().padding(AppSpacing.md), verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            Text(title, style = MaterialTheme.typography.titleSmall)
            Text(detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun FeatureShortcut(title: String, detail: String, icon: androidx.compose.ui.graphics.vector.ImageVector,
                            onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(onClick = onClick, modifier = modifier.heightIn(min = 124.dp)) {
        Column(Modifier.fillMaxWidth().padding(AppSpacing.md), verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun LearningLine(title: String, description: String) {
    ListItem(headlineContent = { Text(title) }, supportingContent = { Text(description) })
    HorizontalDivider()
}

private data class Featured(val title: String, val description: String, val categoryId: String, val demoId: String)
