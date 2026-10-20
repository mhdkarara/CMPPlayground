package com.example.cmpplayground.feature.learn.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cmpplayground.core.designsystem.tokens.AppSpacing

@Composable
fun LearnScreen(onPlatformComparison: () -> Unit = {}) {
    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = AppSpacing.screen, vertical = AppSpacing.screen),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.lg),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Learn", style = MaterialTheme.typography.headlineMedium)
                Text("Start from familiar Swift concepts, then explore what Compose does differently.",
                    style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        item { TopicGroup("Start here", listOf(
            Topic("Swift to Kotlin", "let / var map to val / var; Kotlin makes immutability explicit."),
            Topic("SwiftUI to Compose", "Compare declarative UI, modifiers, and state ownership."),
        )) }
        item { TopicGroup("Building UI", listOf(
            Topic("Layouts", "VStack, HStack, and ZStack have conceptual Compose counterparts."),
            Topic("Modifiers", "Compose Modifier is an ordered value, not a SwiftUI view modifier chain."),
            Topic("State", "Compare @State with remember and state hoisting."),
        )) }
        item { TopicGroup("App architecture", listOf(
            Topic("MVVM", "Keep UI events and observable screen state separate from rendering."),
            Topic("StateFlow", "A Kotlin Flow API for state that can be observed by the UI."),
            Topic("Coroutines", "Use structured asynchronous work with lifecycle-aware scopes."),
        )) }
        item {
            Column {
                Text("Multiplatform", style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 4.dp))
                ListItem(
                    headlineContent = { Text("Shared vs platform code") },
                    supportingContent = { Text("See where common Kotlin ends and native APIs begin.") },
                    trailingContent = { Icon(Icons.Default.Info, contentDescription = null) },
                    modifier = Modifier.clickable(onClick = onPlatformComparison),
                )
                HorizontalDivider()
            }
        }
        item {
            Text("Mappings are conceptual, not one-to-one API replacements. Lifecycle, identity, and state restoration can differ.",
                style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun TopicGroup(title: String, topics: List<Topic>) {
    Column {
        Text(title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 4.dp))
        topics.forEach { topic ->
            ListItem(headlineContent = { Text(topic.title) }, supportingContent = { Text(topic.description) })
            HorizontalDivider()
        }
    }
}

private data class Topic(val title: String, val description: String)
