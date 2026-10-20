package com.example.cmpplayground.feature.catalog.examples

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cmpplayground.feature.catalog.model.CatalogDemo

@Composable
internal fun ColumnScope.LayoutLesson(demo: CatalogDemo) {
    when (demo) {
        CatalogDemo.JourneyColumn -> Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf("08:00 · Depart the station", "10:00 · Walk the cedar trail", "12:30 · Arrive at camp").forEach {
                RouteTile(it)
            }
        }
        CatalogDemo.GearRow -> Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Water", "Layers", "Food").forEach { RouteTile(it, Modifier.weight(1f)) }
        }
        CatalogDemo.PhotoOverlay -> {
            var bookmarked by rememberSaveable { mutableStateOf(false) }
            Box(Modifier.fillMaxWidth().height(220.dp).background(MaterialTheme.colorScheme.secondaryContainer)) {
                Text("CEDAR LOOP", Modifier.align(Alignment.Center), style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer)
                Surface(Modifier.align(Alignment.BottomStart).padding(12.dp), shape = MaterialTheme.shapes.small) {
                    Text("6 km · Woodland", Modifier.padding(8.dp))
                }
                IconToggleButton(bookmarked, { bookmarked = it }, Modifier.align(Alignment.TopEnd)) {
                    Icon(Icons.Default.Favorite, if (bookmarked) "Remove bookmark" else "Bookmark route",
                        tint = if (bookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondaryContainer)
                }
            }
        }
        CatalogDemo.WindowBudget -> BoxWithConstraints(Modifier.fillMaxWidth()) {
            val availableWidth = maxWidth
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Available width: ${availableWidth.value.toInt()} dp")
                if (availableWidth < 360.dp) RouteTile("Cedar loop · 6 km")
                else Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    RouteTile("Cedar loop", Modifier.weight(1f))
                    RouteTile("6 km · Easy", Modifier.weight(1f))
                }
            }
        }
        CatalogDemo.ProfileReflow -> BoxWithConstraints(Modifier.fillMaxWidth()) {
            if (maxWidth >= 480.dp) Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                GuideInitials()
                GuideDescription()
            } else Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                GuideInitials()
                GuideDescription()
            }
        }
        CatalogDemo.TrailCarousel, CatalogDemo.ItineraryList, CatalogDemo.SupplyGrid -> SelectableCollection(demo)
        CatalogDemo.SpacingLab -> {
            var inset by rememberSaveable { mutableFloatStateOf(12f) }
            Slider(inset, { inset = it }, valueRange = 0f..40f)
            Text("Inner padding: ${inset.toInt()} dp")
            Box(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primaryContainer).padding(inset.dp)) {
                Text("Cedar trail meeting point", Modifier.background(MaterialTheme.colorScheme.surface).padding(8.dp))
            }
            LessonNote("The outer background includes the adjustable padding; the inner background begins after it.")
        }
        else -> error("Not a layout lesson: $demo")
    }
}

@Composable
private fun RouteTile(label: String, modifier: Modifier = Modifier, selected: Boolean = false, onClick: (() -> Unit)? = null) {
    val color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh
    if (onClick == null) Surface(modifier, shape = MaterialTheme.shapes.medium, color = color) {
        Text(label, Modifier.padding(16.dp))
    } else Card(onClick = onClick, modifier = modifier, colors = CardDefaults.cardColors(containerColor = color)) {
        Text(label, Modifier.padding(16.dp))
    }
}

@Composable
private fun GuideInitials() {
    Surface(shape = MaterialTheme.shapes.extraLarge, color = MaterialTheme.colorScheme.tertiaryContainer) {
        Text("AL", Modifier.padding(28.dp), style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
private fun GuideDescription() {
    Column {
        Text("Ari · Trail guide", style = MaterialTheme.typography.titleLarge)
        Text("Woodland walks and easy day trips")
    }
}

@Composable
private fun ColumnScope.SelectableCollection(demo: CatalogDemo) {
    var selection by rememberSaveable { mutableIntStateOf(-1) }
    val names = when (demo) {
        CatalogDemo.TrailCarousel -> List(24) { "Trail ${it + 1} · ${it + 2} km" }
        CatalogDemo.ItineraryList -> List(40) { "Stop ${it + 1} · Route checkpoint" }
        else -> List(30) { "Supply ${it + 1}" }
    }
    Text(if (selection < 0) "Choose an item" else "Selected: ${names[selection]}")
    // Fixed viewport prevents nesting an unbounded lazy list inside the lesson scroll.
    when (demo) {
        CatalogDemo.TrailCarousel -> LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(names.size, key = { it }) { i -> RouteTile(names[i], Modifier.width(160.dp), selection == i) { selection = i } }
        }
        CatalogDemo.ItineraryList -> LazyColumn(Modifier.fillMaxWidth().height(320.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(names.size, key = { it }) { i -> RouteTile(names[i], Modifier.fillMaxWidth(), selection == i) { selection = i } }
        }
        else -> LazyVerticalGrid(GridCells.Adaptive(128.dp), Modifier.fillMaxWidth().height(320.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(names.size, key = { it }) { i -> RouteTile(names[i], selected = selection == i) { selection = i } }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TripDashboardLesson() {
    var stops by rememberSaveable { mutableIntStateOf(0) }
    Scaffold(
        topBar = { TopAppBar(title = { Text("Day trip dashboard") }) },
        bottomBar = { BottomAppBar { Text("Local plan · $stops stops", Modifier.padding(horizontal = 16.dp)) } },
        floatingActionButton = {
            FloatingActionButton(onClick = { stops++ }) { Icon(Icons.Default.Add, "Add a trip stop") }
        },
    ) { insets ->
        Column(Modifier.fillMaxSize().padding(insets).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Cedar loop", style = MaterialTheme.typography.headlineSmall)
            Text("Stops planned: $stops")
            LessonNote("Scaffold supplies insets for its own bars. Content applies those insets before adding local padding.")
        }
    }
}
