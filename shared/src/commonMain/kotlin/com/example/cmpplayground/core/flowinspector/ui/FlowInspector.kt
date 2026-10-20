package com.example.cmpplayground.core.flowinspector.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cmpplayground.core.flowinspector.model.FlowEvent
import com.example.cmpplayground.core.flowinspector.recorder.FlowEventRecorder

@Composable
fun FlowInspector(
    events: List<FlowEvent>,
    recordingEnabled: Boolean,
    recorder: FlowEventRecorder,
    onRecordingEnabledChange: (Boolean) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val expandedEvents = remember { mutableStateListOf<Long>() }
    Column(modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = { onExpandedChange(!expanded) }, modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 0.dp)) {
                Text(if (expanded) "Hide MVVM Flow" else "Explore MVVM Flow")
                Spacer(Modifier.width(6.dp))
                Text(if (expanded) "−" else "+", style = MaterialTheme.typography.titleMedium)
            }
            Text("Record", style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Switch(recordingEnabled, onRecordingEnabledChange)
        }
        if (expanded) {
            HorizontalDivider()
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(if (recordingEnabled) "Recent app events" else "Recording paused",
                    style = MaterialTheme.typography.labelLarge)
                TextButton(onClick = recorder::clear, enabled = events.isNotEmpty()) { Text("Clear") }
            }
            if (events.isEmpty()) {
                Text("Use the feature above to see its recorded UI and state events.",
                    Modifier.padding(bottom = 12.dp), style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                events.takeLast(12).forEachIndexed { index, event ->
                    ListItem(
                        leadingContent = {
                            Surface(shape = MaterialTheme.shapes.small, color = MaterialTheme.colorScheme.secondaryContainer) {
                                Text("${index + 1}", Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelMedium)
                            }
                        },
                        headlineContent = { Text(event.layer.name.replace('_', ' '), style = MaterialTheme.typography.labelLarge) },
                        supportingContent = {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(event.description)
                                if (event.id in expandedEvents) {
                                    Text("Operation: ${event.operationId} · Event ${event.id + 1}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        },
                        trailingContent = { Text(event.operationId, style = MaterialTheme.typography.labelSmall) },
                        modifier = Modifier.clickable {
                            if (event.id in expandedEvents) expandedEvents.remove(event.id)
                            else expandedEvents.add(event.id)
                        },
                    )
                }
            }
        }
    }
}
