package com.example.cmpplayground.feature.catalog.examples

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cmpplayground.feature.catalog.model.CatalogDemo
import cmpplayground.shared.generated.resources.Res
import cmpplayground.shared.generated.resources.trail_marker
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun ColumnScope.EssentialsLesson(demo: CatalogDemo) {
    when (demo) {
        CatalogDemo.ReadingType -> {
            Text("Cedar loop", style = MaterialTheme.typography.headlineMedium)
            Text("FIELD JOURNAL · DAY 01", style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary)
            SelectionContainer { Text("Meet at the wooden bridge. Follow the creek until the trail bends north.") }
            LessonNote("Long-press the route note to select it. Typography describes hierarchy without adding state.")
        }
        CatalogDemo.TrailBadge -> {
            Image(painterResource(Res.drawable.trail_marker), "Winding trail between two checkpoints",
                Modifier.fillMaxWidth().height(200.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Image(painterResource(Res.drawable.trail_marker), null, Modifier.size(48.dp))
                Text("The same bundled vector, shown as a small marker.")
            }
        }
        CatalogDemo.SaveActions -> SaveRouteLesson()
        CatalogDemo.SyncAction -> {
            var syncing by rememberSaveable { mutableStateOf(false) }
            var completed by rememberSaveable { mutableIntStateOf(0) }
            LaunchedEffect(syncing) {
                if (syncing) { delay(900); completed++; syncing = false }
            }
            Button(onClick = { syncing = true }, enabled = !syncing) {
                if (syncing) {
                    CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                    Spacer(Modifier.width(8.dp))
                }
                Text(if (syncing) "Syncing plan…" else "Sync plan")
            }
            Text("Completed syncs: $completed")
            LessonNote("This is a local delay, with no network request. Leaving the preview cancels its effect.")
        }
        CatalogDemo.PackingCard -> {
            var ready by rememberSaveable { mutableStateOf(false) }
            Card(onClick = { ready = !ready }, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Water and first aid", style = MaterialTheme.typography.titleMedium)
                    Text(if (ready) "Packed for tomorrow" else "Tap when both are packed")
                }
            }
            LessonNote("The card action changes one piece of local state; its contents render that state.")
        }
        CatalogDemo.StatusSurface -> {
            var ready by rememberSaveable { mutableStateOf(false) }
            Surface(
                color = if (ready) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                contentColor = if (ready) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer,
                shape = MaterialTheme.shapes.large,
            ) { Text(if (ready) "Trip ready to go" else "Trip needs a final check", Modifier.fillMaxWidth().padding(24.dp)) }
            OutlinedButton(onClick = { ready = !ready }) { Text("Change readiness") }
        }
        CatalogDemo.DayDivider -> {
            Text("Morning", style = MaterialTheme.typography.titleMedium)
            Text("08:00 · Meet at Cedar bridge")
            Text("09:30 · Refill at the spring")
            HorizontalDivider()
            Text("Afternoon", style = MaterialTheme.typography.titleMedium)
            Text("13:00 · Lunch at the lookout")
            LessonNote("A separator marks a meaningful change of group, rather than separating every line.")
        }
        CatalogDemo.AvatarShapes -> {
            var corners by rememberSaveable { mutableFloatStateOf(12f) }
            Box(Modifier.size(112.dp).background(MaterialTheme.colorScheme.tertiaryContainer,
                RoundedCornerShape(corners.dp)), contentAlignment = Alignment.Center) {
                Text("TM", style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onTertiaryContainer)
            }
            Slider(corners, { corners = it }, valueRange = 0f..56f)
            Text("Corner radius: ${corners.toInt()} dp")
        }
        CatalogDemo.AddWaypoint -> {
            var waypoints by rememberSaveable { mutableIntStateOf(0) }
            Text("Waypoints in this plan: $waypoints")
            ExtendedFloatingActionButton(onClick = { waypoints++ },
                icon = { Icon(Icons.Default.Add, null) }, text = { Text("Add waypoint") })
            TextButton(onClick = { waypoints = 0 }, enabled = waypoints > 0) { Text("Reset plan") }
        }
        CatalogDemo.UnreadBadge -> {
            var unread by rememberSaveable { mutableIntStateOf(0) }
            BadgedBox(badge = { if (unread > 0) Badge { Text(if (unread > 9) "9+" else "$unread") } }) {
                Icon(Icons.Default.Notifications, "Trail updates: $unread unread", Modifier.size(32.dp))
            }
            Button(onClick = { unread++ }) { Text("Receive update") }
            TextButton(onClick = { unread = 0 }, enabled = unread > 0) { Text("Mark all read") }
            LessonNote("The badge is hidden at zero and caps its displayed count at 9+.")
        }
        else -> error("Not an Essentials lesson: $demo")
    }
}

@Composable
private fun ColumnScope.SaveRouteLesson() {
    var saved by rememberSaveable { mutableStateOf(false) }
    Text(if (saved) "Cedar loop is saved" else "Cedar loop is a draft")
    Button(onClick = { saved = true }, enabled = !saved) { Text("Save route") }
    OutlinedButton(onClick = { saved = false }, enabled = saved) { Text("Undo save") }
    TextButton(onClick = { saved = false }) { Text("Start over") }
    LessonNote("Each button forwards an action. The same saved value drives the message and enabled states.")
}
