package com.example.cmpplayground.feature.catalog.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cmpplayground.core.designsystem.tokens.AppSpacing
import com.example.cmpplayground.feature.catalog.examples.DemoContent
import com.example.cmpplayground.feature.catalog.model.CatalogCategory
import com.example.cmpplayground.feature.catalog.model.CatalogEntry
import com.example.cmpplayground.feature.catalog.model.CatalogDemo

@Composable
fun CatalogScreen(
    categories: List<CatalogCategory>,
    category: CatalogCategory?,
    selectedEntry: CatalogEntry?,
    searchResults: List<CatalogEntry>? = null,
    query: String,
    onQueryChange: (String) -> Unit,
    onCategorySelected: (CatalogCategory) -> Unit,
    onEntrySelected: (CatalogEntry) -> Unit,
) {
    when {
        selectedEntry != null -> DemoContent(selectedEntry.id, category?.title.orEmpty(),
            entryDescription(selectedEntry.id))
        category != null -> LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = AppSpacing.screen, vertical = AppSpacing.sm),
        ) {
            item {
                Column(Modifier.padding(bottom = 12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("${category.demos.size} examples", style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(categoryDescription(category.id), style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            items(category.demos, key = { it.id }) { entry ->
                CatalogRow(entry.title, entryDescription(entry.id)) { onEntrySelected(entry) }
                HorizontalDivider()
            }
        }
        else -> LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = AppSpacing.screen, vertical = AppSpacing.md),
        ) {
            item {
                Text("Explore Compose", style = MaterialTheme.typography.headlineSmall)
                Text("Browse interactive samples, organized by the idea they demonstrate.",
                    Modifier.padding(top = 4.dp, bottom = 16.dp), style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                OutlinedTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    placeholder = { Text("Search 43 examples") },
                    shape = MaterialTheme.shapes.medium,
                )
            }
            if (searchResults != null) {
                if (searchResults.isEmpty()) item {
                    EmptySearchState(query)
                } else {
                    item { Text("${searchResults.size} results", style = MaterialTheme.typography.labelLarge) }
                    items(searchResults, key = { it.id }) { entry ->
                        CatalogRow(entry.title,
                            "${categories.first { it.id == entry.categoryId }.title} · ${entryDescription(entry.id)}") {
                            onEntrySelected(entry)
                        }
                        HorizontalDivider()
                    }
                }
            } else {
                items(categories, key = { it.id }) { item ->
                    ListItem(
                        headlineContent = { Text(item.title) },
                        supportingContent = { Text(categoryDescription(item.id)) },
                        trailingContent = { Text("${item.demos.size}", color = MaterialTheme.colorScheme.primary) },
                        modifier = Modifier.fillMaxWidth().clickable { onCategorySelected(item) },
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun EmptySearchState(query: String) {
    Column(Modifier.fillMaxWidth().padding(vertical = 36.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("No matches", style = MaterialTheme.typography.titleMedium)
        Text("No component matches “$query”. Try a shorter or different term.",
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

private fun categoryDescription(id: String) = when (id) {
    "essentials" -> "Core UI building blocks and common Material components."
    "layout" -> "Arrange, size, scroll, and compose content."
    "input" -> "Collect and represent user input."
    "feedback" -> "Communicate progress, decisions, and transient messages."
    "motion" -> "Animate transitions between UI states."
    else -> "Platform behavior and lower-level Compose techniques."
}

private fun entryDescription(id: String) = CatalogDemo.fromId(id)?.goal.orEmpty()
