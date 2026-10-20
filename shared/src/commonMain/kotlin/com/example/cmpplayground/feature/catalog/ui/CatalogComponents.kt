package com.example.cmpplayground.feature.catalog.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CatalogRow(title: String, subtitle: String? = null, onClick: () -> Unit) {
    ListItem(headlineContent = { Text(title) },
        supportingContent = subtitle?.let { { Text(it) } },
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).heightIn(min = 56.dp))
}
