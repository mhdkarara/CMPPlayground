package com.example.cmpplayground.feature.catalog.examples

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cmpplayground.core.codecomparison.ui.CodeComparison
import com.example.cmpplayground.feature.catalog.model.CatalogDemo
import com.example.cmpplayground.feature.learn.ui.PlatformComparisonScreen
import com.example.cmpplayground.feature.playground.calculator.CalculatorRoute

@Composable
fun DemoContent(id: String, categoryTitle: String = "", description: String = "") {
    val demo = requireNotNull(CatalogDemo.fromId(id)) { "Unknown lesson: $id" }
    when (demo) {
        CatalogDemo.NativeControls -> PlatformComparisonScreen(categoryTitle, demo.goal)
        CatalogDemo.Calculator -> CalculatorRoute()
        CatalogDemo.TripScaffold -> TripDashboardLesson()
        CatalogDemo.TripDrawer -> TripFoldersLesson()
        CatalogDemo.PlannerRail -> PlannerRailLesson()
        else -> Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(categoryTitle, style = MaterialTheme.typography.labelLarge)
            Text(demo.title, style = MaterialTheme.typography.headlineSmall)
            Text(description.ifBlank { demo.goal }, style = MaterialTheme.typography.bodyMedium)
            HorizontalDivider()
            LessonPreview(demo)
            EssentialsCodeExamples.byDemoId[demo.id]?.let { example ->
                HorizontalDivider()
                CodeComparison(example)
            }
        }
    }
}

// An exhaustive dispatch makes a new lesson require an explicit rendering decision.
@Composable
private fun ColumnScope.LessonPreview(demo: CatalogDemo) {
    when (demo) {
        CatalogDemo.ReadingType, CatalogDemo.TrailBadge, CatalogDemo.SaveActions,
        CatalogDemo.SyncAction, CatalogDemo.PackingCard, CatalogDemo.StatusSurface,
        CatalogDemo.DayDivider, CatalogDemo.AvatarShapes, CatalogDemo.AddWaypoint,
        CatalogDemo.UnreadBadge -> EssentialsLesson(demo)
        CatalogDemo.JourneyColumn, CatalogDemo.GearRow, CatalogDemo.PhotoOverlay,
        CatalogDemo.WindowBudget, CatalogDemo.ProfileReflow, CatalogDemo.TrailCarousel,
        CatalogDemo.ItineraryList, CatalogDemo.SupplyGrid, CatalogDemo.SpacingLab -> LayoutLesson(demo)
        CatalogDemo.TrailName, CatalogDemo.SearchQuery, CatalogDemo.TripNotes,
        CatalogDemo.GearChecklist, CatalogDemo.PaceChoice, CatalogDemo.OfflineSwitch,
        CatalogDemo.DistanceSlider, CatalogDemo.RegionMenu -> InputLesson(demo)
        CatalogDemo.DiscardDialog, CatalogDemo.TripProgress, CatalogDemo.TransferProgress,
        CatalogDemo.RouteSheet, CatalogDemo.UndoMessage -> FeedbackLesson(demo)
        CatalogDemo.WeatherCrossfade, CatalogDemo.ElevationCanvas, CatalogDemo.SessionLifecycle,
        CatalogDemo.DraftBack, CatalogDemo.MeasuredLabels, CatalogDemo.TrailToolbar -> AdvancedLesson(demo)
        CatalogDemo.TripScaffold, CatalogDemo.TripDrawer, CatalogDemo.PlannerRail,
        CatalogDemo.NativeControls, CatalogDemo.Calculator -> error("Lesson requires its own screen")
    }
}

@Composable
internal fun LessonNote(text: String) {
    Text(text, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
}
