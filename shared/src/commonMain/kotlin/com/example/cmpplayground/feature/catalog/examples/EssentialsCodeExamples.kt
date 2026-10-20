package com.example.cmpplayground.feature.catalog.examples

import com.example.cmpplayground.core.codecomparison.model.CodeExample
import com.example.cmpplayground.feature.catalog.examples.material.ButtonCodeExamples
import com.example.cmpplayground.feature.catalog.model.CatalogDemo

/** Focused excerpts for this app's trail-planning scenarios, not standalone files. */
object EssentialsCodeExamples {
    private fun example(demo: CatalogDemo, swift: String, kotlin: String, explanation: String, difference: String) =
        demo.id to CodeExample(demo.title, demo.goal, swift, kotlin, explanation, listOf(difference))

    val byDemoId: Map<String, CodeExample> = mapOf(
        CatalogDemo.SaveActions.id to ButtonCodeExamples.comparison,
        example(CatalogDemo.ReadingType,
            swift = """VStack(alignment: .leading) {
    Text("Cedar loop").font(.title)
    Text("FIELD JOURNAL · DAY 01").font(.caption)
    Text("Meet at the wooden bridge.").textSelection(.enabled)
}""",
            kotlin = """Column {
    Text("Cedar loop", style = MaterialTheme.typography.headlineMedium)
    Text("FIELD JOURNAL · DAY 01", style = MaterialTheme.typography.labelLarge)
    SelectionContainer { Text("Meet at the wooden bridge.") }
}""",
            explanation = "A route journal uses hierarchy to distinguish the destination, metadata, and directions. These excerpts focus on the text structure.",
            difference = "SwiftUI enables text selection with a modifier; Compose wraps selectable text in SelectionContainer."),
        example(CatalogDemo.TrailBadge,
            swift = """// Add your trail_marker artwork to the Xcode asset catalog first.
Image("trail_marker")
    .resizable()
    .scaledToFit()
    .frame(height: 200)
    .accessibilityLabel("Winding trail between two checkpoints")""",
            kotlin = """Image(
    painter = painterResource(Res.drawable.trail_marker),
    contentDescription = "Winding trail between two checkpoints",
    modifier = Modifier.fillMaxWidth().height(200.dp),
)""",
            explanation = "The live preview uses our bundled vector at two sizes. The SwiftUI excerpt assumes you have added a corresponding Xcode asset.",
            difference = "Compose generates Res accessors from composeResources; SwiftUI looks up its own asset catalog."),
        example(CatalogDemo.SyncAction,
            swift = """@State private var syncing = false
@State private var completed = 0

Button {
    syncing = true
} label: {
    if syncing { ProgressView() } else { Text("Sync plan") }
}
.disabled(syncing)
.task(id: syncing) {
    guard syncing else { return }
    do { try await Task.sleep(for: .milliseconds(900)) }
    catch { return }
    completed += 1
    syncing = false
}""",
            kotlin = """var syncing by rememberSaveable { mutableStateOf(false) }
var completed by rememberSaveable { mutableIntStateOf(0) }
LaunchedEffect(syncing) {
    if (syncing) {
        delay(900)
        completed++
        syncing = false
    }
}
Button(onClick = { syncing = true }, enabled = !syncing) {
    if (syncing) CircularProgressIndicator(Modifier.size(18.dp))
    else Text("Sync plan")
}""",
            explanation = "A local delay simulates sync. View-owned work is cancelled when its view or composition leaves; this example does not contact a server.",
            difference = "SwiftUI task(id:) and Compose LaunchedEffect(key) restart work when their key changes, subject to their respective lifecycle rules."),
        example(CatalogDemo.PackingCard,
            swift = """@State private var ready = false
Button { ready.toggle() } label: {
    VStack(alignment: .leading) {
        Text("Water and first aid").font(.headline)
        Text(ready ? "Packed for tomorrow" : "Tap when both are packed")
    }
    .padding(20)
}
.buttonStyle(.bordered)""",
            kotlin = """var ready by rememberSaveable { mutableStateOf(false) }
Card(onClick = { ready = !ready }) {
    Column(Modifier.padding(20.dp)) {
        Text("Water and first aid", style = MaterialTheme.typography.titleMedium)
        Text(if (ready) "Packed for tomorrow" else "Tap when both are packed")
    }
}""",
            explanation = "The whole reminder is one action surface. Its message is derived from one ready value.",
            difference = "The SwiftUI example groups content inside a Button; Compose offers an interactive Card with Material styling."),
        example(CatalogDemo.StatusSurface,
            swift = """@State private var ready = false
Text(ready ? "Trip ready to go" : "Trip needs a final check")
    .padding(24)
    .frame(maxWidth: .infinity)
    .background(ready ? Color.green.opacity(0.2) : Color.orange.opacity(0.2),
                in: RoundedRectangle(cornerRadius: 20))
Button("Change readiness") { ready.toggle() }""",
            kotlin = """var ready by rememberSaveable { mutableStateOf(false) }
Surface(
    color = if (ready) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.secondaryContainer,
    shape = MaterialTheme.shapes.large,
) {
    Text(if (ready) "Trip ready to go" else "Trip needs a final check",
        Modifier.fillMaxWidth().padding(24.dp))
}
OutlinedButton(onClick = { ready = !ready }) { Text("Change readiness") }""",
            explanation = "The live preview pairs Material container and content colors. Its message also expresses readiness, so color is not the only cue.",
            difference = "Material Surface provides container and content color behavior; SwiftUI composes background and foreground styling."),
        example(CatalogDemo.DayDivider,
            swift = """VStack(alignment: .leading, spacing: 16) {
    Text("Morning").font(.headline)
    Text("08:00 · Meet at Cedar bridge")
    Divider()
    Text("Afternoon").font(.headline)
    Text("13:00 · Lunch at the lookout")
}""",
            kotlin = """Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
    Text("Morning", style = MaterialTheme.typography.titleMedium)
    Text("08:00 · Meet at Cedar bridge")
    HorizontalDivider()
    Text("Afternoon", style = MaterialTheme.typography.titleMedium)
    Text("13:00 · Lunch at the lookout")
}""",
            explanation = "A divider marks a meaningful transition in the itinerary. Spacing keeps related details together.",
            difference = "Material Compose names the orientation explicitly with HorizontalDivider; SwiftUI Divider responds to the surrounding layout."),
        example(CatalogDemo.AvatarShapes,
            swift = """@State private var radius = 12.0
Text("TM")
    .frame(width: 112, height: 112)
    .background(.mint, in: RoundedRectangle(cornerRadius: radius))
Slider(value: ${'$'}radius, in: 0...56)""",
            kotlin = """var radius by rememberSaveable { mutableFloatStateOf(12f) }
Box(
    Modifier.size(112.dp).background(
        MaterialTheme.colorScheme.tertiaryContainer,
        RoundedCornerShape(radius.dp),
    ),
    contentAlignment = Alignment.Center,
) { Text("TM") }
Slider(radius, { radius = it }, valueRange = 0f..56f)""",
            explanation = "The slider changes the geometry of a team marker rather than choosing another asset.",
            difference = "Compose expresses the corner radius in dp; SwiftUI uses points. Both derive geometry from observable state."),
        example(CatalogDemo.AddWaypoint,
            swift = """@State private var waypoints = 0
Text("Waypoints in this plan: \(waypoints)")
Button { waypoints += 1 } label: {
    Label("Add waypoint", systemImage: "plus")
}
.buttonStyle(.borderedProminent)""",
            kotlin = """var waypoints by rememberSaveable { mutableIntStateOf(0) }
Text("Waypoints in this plan: ${'$'}waypoints")
ExtendedFloatingActionButton(
    onClick = { waypoints++ },
    icon = { Icon(Icons.Default.Add, null) },
    text = { Text("Add waypoint") },
)""",
            explanation = "A labeled primary action changes a local count. The separate Scaffold lesson demonstrates floating placement at screen level.",
            difference = "Compose has a dedicated Material extended FAB; the SwiftUI example communicates the action through a prominent labeled button."),
        example(CatalogDemo.UnreadBadge,
            swift = """@State private var unread = 0
HStack {
    Image(systemName: "bell")
    if unread > 0 { Text(unread > 9 ? "9+" : "\(unread)") }
}
.accessibilityElement(children: .ignore)
.accessibilityLabel("Trail updates: \(unread) unread")
Button("Receive update") { unread += 1 }
Button("Mark all read") { unread = 0 }""",
            kotlin = """var unread by rememberSaveable { mutableIntStateOf(0) }
BadgedBox(badge = {
    if (unread > 0) Badge { Text(if (unread > 9) "9+" else "${'$'}unread") }
}) {
    Icon(Icons.Default.Notifications, "Trail updates: ${'$'}unread unread")
}
Button(onClick = { unread++ }) { Text("Receive update") }
TextButton(onClick = { unread = 0 }) { Text("Mark all read") }""",
            explanation = "Zero hides the badge. The visible count caps at 9+, while the description retains the actual count.",
            difference = "Compose BadgedBox positions an attached badge; the SwiftUI excerpt uses an inline count, so their layouts differ."),
    )
}
