package com.example.cmpplayground.feature.catalog.examples

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cmpplayground.feature.catalog.model.CatalogDemo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ColumnScope.FeedbackLesson(demo: CatalogDemo) {
    when (demo) {
        CatalogDemo.DiscardDialog -> {
            var asking by rememberSaveable { mutableStateOf(false) }
            var draft by rememberSaveable { mutableStateOf(true) }
            Text(if (draft) "An unsaved route draft is available" else "Draft removed")
            Button(onClick = { asking = true }, enabled = draft) { Text("Discard draft") }
            TextButton(onClick = { draft = true }) { Text("Create a new draft") }
            if (asking) AlertDialog(onDismissRequest = { asking = false },
                title = { Text("Discard route draft?") },
                text = { Text("This removes the draft from this preview. Your saved routes are unaffected.") },
                confirmButton = { TextButton(onClick = { draft = false; asking = false }) { Text("Discard") } },
                dismissButton = { TextButton(onClick = { asking = false }) { Text("Keep editing") } })
        }
        CatalogDemo.TripProgress -> {
            var packed by rememberSaveable { mutableIntStateOf(0) }
            CircularProgressIndicator(progress = { packed / 5f }, modifier = Modifier.size(88.dp))
            Text("$packed of 5 supplies packed")
            Button(onClick = { packed++ }, enabled = packed < 5) { Text("Pack next supply") }
            TextButton(onClick = { packed = 0 }) { Text("Unpack") }
        }
        CatalogDemo.TransferProgress -> {
            var running by rememberSaveable { mutableStateOf(false) }
            var progress by rememberSaveable { mutableIntStateOf(0) }
            LaunchedEffect(running) {
                if (running) {
                    while (progress < 10) { delay(180); progress++ }
                    running = false
                }
            }
            LinearProgressIndicator(progress = { progress / 10f }, modifier = Modifier.fillMaxWidth())
            Text("Map preview: ${progress * 10}%")
            Button(onClick = { progress = 0; running = true }, enabled = !running) { Text("Simulate download") }
            TextButton(onClick = { running = false }, enabled = running) { Text("Cancel") }
            LessonNote("The effect is cancelled when stopped or removed from composition. No map data is fetched.")
        }
        CatalogDemo.RouteSheet -> {
            var open by rememberSaveable { mutableStateOf(false) }
            var selected by rememberSaveable { mutableStateOf(false) }
            Text(if (selected) "Cedar loop added to your local plan" else "No route selected")
            Button(onClick = { open = true }) { Text("Inspect Cedar loop") }
            if (open) ModalBottomSheet(onDismissRequest = { open = false }) {
                Column(Modifier.fillMaxWidth().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Cedar loop", style = MaterialTheme.typography.headlineSmall)
                    Text("6 km · Easy · Starts at the wooden bridge")
                    Button(onClick = { selected = true; open = false }) { Text("Choose this route") }
                    TextButton(onClick = { open = false }) { Text("Close details") }
                }
            }
        }
        CatalogDemo.UndoMessage -> {
            val host = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            var present by rememberSaveable { mutableStateOf(true) }
            var pending by remember { mutableStateOf(false) }
            Text(if (present) "Lunch stop is in your plan" else "Lunch stop removed")
            Button(enabled = present && !pending, onClick = {
                present = false
                pending = true
                scope.launch {
                    try {
                        if (host.showSnackbar("Lunch stop removed", actionLabel = "Restore",
                            withDismissAction = true) == SnackbarResult.ActionPerformed) present = true
                    } finally { pending = false }
                }
            }) { Text("Remove lunch stop") }
            TextButton(onClick = { present = true }, enabled = !pending) { Text("Reset example") }
            SnackbarHost(host)
        }
        else -> error("Not a feedback lesson: $demo")
    }
}

@Composable
internal fun TripFoldersLesson() {
    val drawer = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var folder by rememberSaveable { mutableStateOf("Upcoming") }
    ModalNavigationDrawer(drawerState = drawer, drawerContent = {
        ModalDrawerSheet {
            Text("Trip folders", Modifier.padding(20.dp), style = MaterialTheme.typography.titleLarge)
            listOf("Upcoming", "Saved routes", "Completed").forEach { name ->
                NavigationDrawerItem(label = { Text(name) }, selected = folder == name, onClick = {
                    folder = name
                    scope.launch { drawer.close() }
                })
            }
        }
    }) {
        Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(folder, style = MaterialTheme.typography.headlineSmall)
            Button(onClick = { scope.launch { drawer.open() } }) { Text("Choose a folder") }
            LessonNote("The drawer owns its open/closed animation; the selected folder is separate saved UI state.")
        }
    }
}
