package com.example.cmpplayground.feature.catalog.examples

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.constrainHeight
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.cmpplayground.CatalogBackHandler
import com.example.cmpplayground.feature.catalog.model.CatalogDemo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ColumnScope.AdvancedLesson(demo: CatalogDemo) {
    when (demo) {
        CatalogDemo.WeatherCrossfade -> {
            var rain by rememberSaveable { mutableStateOf(false) }
            OutlinedButton(onClick = { rain = !rain }) { Text("Change forecast") }
            Crossfade(targetState = rain) { wet ->
                Surface(color = if (wet) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.tertiaryContainer,
                    shape = MaterialTheme.shapes.large, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(if (wet) "Rain after lunch" else "Clear skies", style = MaterialTheme.typography.headlineSmall)
                        Text(if (wet) "Pack a shell and choose the sheltered route." else "A good day for the ridge lookout.")
                    }
                }
            }
            LessonNote("The target Boolean chooses the content; Crossfade animates the transition between its values.")
        }
        CatalogDemo.ElevationCanvas -> {
            var checkpoint by rememberSaveable { mutableFloatStateOf(0.5f) }
            val ink = MaterialTheme.colorScheme.primary
            val marker = MaterialTheme.colorScheme.tertiary
            val elevations = listOf(0.8f, 0.7f, 0.35f, 0.5f, 0.18f, 0.4f, 0.7f)
            Canvas(Modifier.fillMaxWidth().height(180.dp)) {
                val points = elevations.mapIndexed { i, y -> Offset(size.width * i / elevations.lastIndex, size.height * y) }
                points.zipWithNext().forEach { (start, end) -> drawLine(ink, start, end, strokeWidth = 4.dp.toPx()) }
                val segment = (checkpoint * elevations.lastIndex).toInt().coerceAtMost(elevations.lastIndex - 1)
                val fraction = checkpoint * elevations.lastIndex - segment
                val y = elevations[segment] + (elevations[segment + 1] - elevations[segment]) * fraction
                drawCircle(marker, radius = 7.dp.toPx(), center = Offset(size.width * checkpoint, size.height * y))
            }
            Slider(checkpoint, { checkpoint = it })
            Text("Checkpoint: ${(checkpoint * 100).toInt()}% of route")
        }
        CatalogDemo.SessionLifecycle -> {
            val owner = LocalLifecycleOwner.current
            val events = remember(owner) { mutableStateListOf("Current: ${owner.lifecycle.currentState}") }
            DisposableEffect(owner) {
                val observer = LifecycleEventObserver { _, event ->
                    events.add(event.name)
                    if (events.size > 12) events.removeAt(0)
                }
                owner.lifecycle.addObserver(observer)
                onDispose { owner.lifecycle.removeObserver(observer) }
            }
            Text("Recent host events", style = MaterialTheme.typography.titleMedium)
            events.forEach { Text(it) }
            LessonNote("Background and foreground the host to observe callbacks. The observer is removed when this lesson leaves composition.")
        }
        CatalogDemo.DraftBack -> {
            var editing by rememberSaveable { mutableStateOf(false) }
            CatalogBackHandler(editing) { editing = false }
            if (editing) {
                Text("Editing the meeting-point draft")
                OutlinedButton(onClick = { editing = false }) { Text("Return to route summary") }
                LessonNote("On Android, system back closes this editor first. The button provides the same local action on both platforms.")
            } else {
                Text("Route summary")
                Button(onClick = { editing = true }) { Text("Open local editor") }
            }
        }
        CatalogDemo.MeasuredLabels -> {
            EqualRouteLabels()
            LessonNote("Subcompose once to measure natural widths, then compose display rows at a common width. The labels here are side-effect-free.")
        }
        CatalogDemo.TrailToolbar -> {
            var marked by rememberSaveable { mutableStateOf(false) }
            TopAppBar(title = { Text("Cedar loop") }, actions = {
                IconToggleButton(marked, { marked = it }) {
                    Icon(Icons.Default.Favorite, if (marked) "Remove saved route" else "Save route",
                        tint = if (marked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                }
            })
            Text(if (marked) "Route added to saved plans" else "Route is not saved")
        }
        else -> error("Not an advanced lesson: $demo")
    }
}

@Composable
private fun EqualRouteLabels() {
    val labels = listOf("Start", "Creek crossing", "North lookout")
    SubcomposeLayout(Modifier.fillMaxWidth()) { bounds ->
        val natural = subcompose("probe") { labels.forEach { Text(it, Modifier.padding(12.dp)) } }
            .map { it.measure(bounds.copy(minWidth = 0, minHeight = 0)) }
        val width = natural.maxOfOrNull { it.width } ?: 0
        val rows = subcompose("rows") {
            labels.forEach { label ->
                Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = MaterialTheme.shapes.small) {
                    Text(label, Modifier.padding(12.dp))
                }
            }
        }.map { it.measure(bounds.copy(minWidth = width, maxWidth = width, minHeight = 0)) }
        val gap = 6.dp.roundToPx()
        val contentHeight = rows.sumOf { it.height } + gap * (rows.size - 1).coerceAtLeast(0)
        layout(bounds.maxWidth, bounds.constrainHeight(contentHeight)) {
            var top = 0
            rows.forEach { row -> row.placeRelative(0, top); top += row.height + gap }
        }
    }
}

@Composable
internal fun PlannerRailLesson() {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    val labels = listOf("Map", "Packing", "Notes")
    val icons = listOf(Icons.Default.Home, Icons.Default.Settings, Icons.Default.Info)
    Row(Modifier.fillMaxSize()) {
        NavigationRail {
            labels.forEachIndexed { index, label ->
                NavigationRailItem(selected = tab == index, onClick = { tab = index },
                    icon = { Icon(icons[index], null) }, label = { Text(label) })
            }
        }
        Column(Modifier.weight(1f).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(labels[tab], style = MaterialTheme.typography.headlineSmall)
            Text(when (tab) {
                0 -> "Cedar bridge → North lookout"
                1 -> "Water, map, and a light jacket"
                else -> "Meet at the station at 08:00"
            })
            LessonNote("This rail changes local content. App-level navigation remains in the surrounding shell.")
        }
    }
}
