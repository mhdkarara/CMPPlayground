package com.example.cmpplayground.feature.catalog.model

// Learning scenarios owned by this application. IDs are saved navigation values.
enum class CatalogDemo(val id: String, val title: String, val categoryId: String, val goal: String) {
    ReadingType("reading-type", "Trail journal typography", "essentials", "Compare title, body, and selectable route notes."),
    TrailBadge("trail-badge", "Trail marker image", "essentials", "Render our bundled trail marker at two sizes."),
    SaveActions("save-actions", "Buttons: save a route", "essentials", "Save, undo, and reset a route using different button styles."),
    SyncAction("sync-action", "Loading button: sync plan", "essentials", "Run a simulated sync and prevent duplicate requests."),
    PackingCard("packing-card", "Card: packing reminder", "essentials", "Toggle a packing card between ready and incomplete."),
    StatusSurface("status-surface", "Surface: trip readiness", "essentials", "Change a themed container with trip readiness state."),
    DayDivider("day-divider", "Divider: daily itinerary", "essentials", "Separate morning and afternoon itinerary groups."),
    AvatarShapes("avatar-shapes", "Shapes: team markers", "essentials", "Adjust the corner radius of a team marker."),
    AddWaypoint("add-waypoint", "Floating action button: waypoints", "essentials", "Add local waypoints with a prominent action."),
    UnreadBadge("unread-badge", "Badge: trail updates", "essentials", "Add and read local updates with an unread count."),
    JourneyColumn("journey-column", "Column: trip timeline", "layout", "Arrange departure, walking, and arrival vertically."),
    GearRow("gear-row", "Row: gear summary", "layout", "Share horizontal space between three gear groups."),
    PhotoOverlay("photo-overlay", "Box: route overlay", "layout", "Layer a route label and bookmark over a backdrop."),
    WindowBudget("window-budget", "Constraints: available space", "layout", "Inspect available width and switch to a compact summary."),
    ProfileReflow("profile-reflow", "Adaptive layout: guide profile", "layout", "Reflow a guide profile when width permits a horizontal layout."),
    TrailCarousel("trail-carousel", "Lazy row: suggested trails", "layout", "Select a trail in a horizontal scrolling collection."),
    ItineraryList("itinerary-list", "Lazy column: itinerary", "layout", "Select a stop in a vertically scrolling itinerary."),
    SupplyGrid("supply-grid", "Lazy grid: supply shelf", "layout", "Select supplies in a grid that adapts its column count."),
    TripScaffold("trip-scaffold", "Scaffold: trip dashboard", "layout", "Keep content clear of an app bar, bottom bar, and action button."),
    SpacingLab("spacing-lab", "Modifier padding: route label", "layout", "Adjust inner spacing and inspect modifier order."),
    TrailName("trail-name", "Basic text input: trail name", "input", "Edit a name with an undecorated text input."),
    SearchQuery("search-query", "Text field: stop search", "input", "Filter a small list using a filled text field."),
    TripNotes("trip-notes", "Outlined text field: trip notes", "input", "Edit multiline notes and inspect a live character count."),
    GearChecklist("gear-checklist", "Checkbox: gear checklist", "input", "Track three independent packing choices."),
    PaceChoice("pace-choice", "Radio buttons: walking pace", "input", "Choose one walking pace for the trip."),
    OfflineSwitch("offline-switch", "Switch: offline mode", "input", "Toggle a local preference and its explanatory text."),
    DistanceSlider("distance-slider", "Slider: daily distance", "input", "Choose a distance target in whole-kilometer steps."),
    RegionMenu("region-menu", "Dropdown: trail region", "input", "Choose a region from a popup menu."),
    DiscardDialog("discard-dialog", "Alert dialog: discard draft", "feedback", "Confirm or cancel removing a local draft."),
    TripProgress("trip-progress", "Circular progress: packing", "feedback", "Advance a determinate packing indicator."),
    TransferProgress("transfer-progress", "Linear progress: map download", "feedback", "Simulate a cancellable map download without network access."),
    RouteSheet("route-sheet", "Bottom sheet: route details", "feedback", "Open a modal summary and choose a route."),
    TripDrawer("trip-drawer", "Navigation drawer: trip folders", "feedback", "Choose a folder from a modal drawer."),
    UndoMessage("undo-message", "Snackbar: undo removal", "feedback", "Remove a stop and restore it through a snackbar action."),
    WeatherCrossfade("weather-crossfade", "Crossfade: weather outlook", "motion", "Animate between a clear and rainy route forecast."),
    ElevationCanvas("elevation-canvas", "Canvas: elevation sketch", "advanced", "Draw an original elevation profile with a movable checkpoint."),
    SessionLifecycle("session-lifecycle", "Lifecycle: session events", "advanced", "Observe a bounded list of host lifecycle callbacks."),
    DraftBack("draft-back", "Back handling: draft editor", "advanced", "Handle back inside a local editor before leaving the example."),
    MeasuredLabels("measured-labels", "Subcompose layout: equal labels", "advanced", "Measure short route labels before laying out equal-width rows."),
    TrailToolbar("trail-toolbar", "Top app bar: route bookmark", "advanced", "Toggle a bookmark action in a route toolbar."),
    PlannerRail("planner-rail", "Navigation rail: planner", "advanced", "Switch between a map, packing list, and trip notes."),
    NativeControls("native-controls", "Shared vs platform code", "advanced", "Compare shared state with native switches, keyboard hints, and haptic requests."),
    Calculator("calculator", "MVVM calculator", "advanced", "Trace calculator actions through a shared model, ViewModel, and StateFlow."),
    ;

    companion object {
        fun fromId(id: String): CatalogDemo? = entries.firstOrNull { it.id == id }
    }
}
