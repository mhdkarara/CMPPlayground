package com.example.cmpplayground.feature.catalog.examples

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.cmpplayground.feature.catalog.model.CatalogDemo

@Composable
internal fun ColumnScope.InputLesson(demo: CatalogDemo) {
    when (demo) {
        CatalogDemo.TrailName -> {
            var name by rememberSaveable { mutableStateOf("Cedar loop") }
            Text("Trail name", style = MaterialTheme.typography.labelLarge)
            BasicTextField(name, { name = it }, Modifier.fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.small).padding(16.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                singleLine = true)
            Text("Preview: ${name.ifBlank { "Untitled trail" }}")
            LessonNote("BasicTextField handles editing; the surrounding label, border, and padding belong to this lesson.")
        }
        CatalogDemo.SearchQuery -> {
            var query by rememberSaveable { mutableStateOf("") }
            TextField(query, { query = it }, label = { Text("Search stops") }, singleLine = true,
                modifier = Modifier.fillMaxWidth())
            val matches = listOf("Cedar bridge", "River bend", "North lookout").filter { it.contains(query, ignoreCase = true) }
            matches.forEach { Text(it) }
            if (matches.isEmpty()) Text("No stop matches this search")
        }
        CatalogDemo.TripNotes -> {
            var notes by rememberSaveable { mutableStateOf("") }
            OutlinedTextField(notes, { notes = it }, Modifier.fillMaxWidth(),
                label = { Text("Trip notes") }, placeholder = { Text("Meeting point, supplies, reminders…") },
                minLines = 3, supportingText = { Text("${notes.length} characters") })
            LessonNote("The count is derived from the same value the field edits; there is no second counter to synchronize.")
        }
        CatalogDemo.GearChecklist -> {
            var water by rememberSaveable { mutableStateOf(false) }
            var jacket by rememberSaveable { mutableStateOf(false) }
            var map by rememberSaveable { mutableStateOf(false) }
            PackingChoice("Water bottle", water) { water = it }
            PackingChoice("Rain jacket", jacket) { jacket = it }
            PackingChoice("Offline map", map) { map = it }
            Text("${listOf(water, jacket, map).count { it }} of 3 packed")
        }
        CatalogDemo.PaceChoice -> {
            var pace by rememberSaveable { mutableStateOf("Relaxed") }
            listOf("Relaxed", "Steady", "Brisk").forEach { option ->
                Row(Modifier.fillMaxWidth().selectable(pace == option, role = Role.RadioButton, onClick = { pace = option })
                    .padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(pace == option, onClick = null)
                    Text(option)
                }
            }
            Text("Trip pace: $pace")
        }
        CatalogDemo.OfflineSwitch -> {
            var offline by rememberSaveable { mutableStateOf(false) }
            Row(Modifier.fillMaxWidth().toggleable(offline, role = Role.Switch, onValueChange = { offline = it }),
                verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Use offline map")
                    LessonNote(if (offline) "Local map selected" else "Online map selected")
                }
                Switch(offline, onCheckedChange = null)
            }
            LessonNote("This preference changes only the preview; it does not download maps or change network settings.")
        }
        CatalogDemo.DistanceSlider -> {
            var distance by rememberSaveable { mutableFloatStateOf(8f) }
            Text("Daily target: ${distance.toInt()} km")
            Slider(distance, { distance = it }, valueRange = 2f..20f, steps = 17)
            LessonNote("Seventeen intermediate stops plus the endpoints give whole-kilometer choices from 2 to 20.")
        }
        CatalogDemo.RegionMenu -> {
            var expanded by remember { mutableStateOf(false) }
            var region by rememberSaveable { mutableStateOf("Woodland") }
            Box {
                OutlinedButton(onClick = { expanded = true }) { Text("Region: $region") }
                DropdownMenu(expanded, onDismissRequest = { expanded = false }) {
                    listOf("Woodland", "Coast", "Highland").forEach { option ->
                        DropdownMenuItem(text = { Text(option) }, onClick = { region = option; expanded = false })
                    }
                }
            }
            Text("Showing routes in $region")
        }
        else -> error("Not an input lesson: $demo")
    }
}

@Composable
private fun PackingChoice(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().toggleable(checked, role = Role.Checkbox, onValueChange = onCheckedChange)
        .padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked, onCheckedChange = null)
        Text(label)
    }
}
