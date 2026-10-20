package com.example.cmpplayground.core.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.example.cmpplayground.feature.catalog.registry.CatalogRegistry
import com.example.cmpplayground.core.designsystem.tokens.AppSpacing
import com.example.cmpplayground.CatalogBackHandler
import com.example.cmpplayground.feature.catalog.ui.CatalogScreen
import com.example.cmpplayground.feature.home.ui.HomeScreen
import com.example.cmpplayground.feature.learn.ui.LearnScreen
import com.example.cmpplayground.feature.playground.ui.PlaygroundScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    var activeSection by rememberSaveable { mutableStateOf("Discover") }
    var categoryId by rememberSaveable { mutableStateOf<String?>(null) }
    var demoId by rememberSaveable { mutableStateOf<String?>(null) }
    var query by rememberSaveable { mutableStateOf("") }
    var playgroundFeature by rememberSaveable { mutableStateOf("Counter") }
    val categories = CatalogRegistry.categories
    val category = categories.firstOrNull { it.id == categoryId }
    val demo = CatalogRegistry.allEntries.firstOrNull { it.id == demoId }
    val searchResults = query.takeIf { it.isNotBlank() }?.let(CatalogRegistry::search)
    val back: () -> Unit = {
        when {
            demoId != null -> demoId = null
            categoryId != null -> categoryId = null
            else -> query = ""
        }
    }
    CatalogBackHandler(demoId != null || categoryId != null, back)
    BoxWithConstraints(Modifier.fillMaxSize()) {
      val useRail = maxWidth >= AppSpacing.tabletBreakpoint
      Scaffold(topBar = {
            TopAppBar(
                title = { Text(demo?.title ?: category?.title ?: activeSection,
                        maxLines = 1, overflow = TextOverflow.Ellipsis)
                },
                navigationIcon = {
                    if (category != null || demo != null) IconButton(back) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface)
            )
        }, bottomBar = {
            if (!useRail) {
                NavigationBar {
                    listOf("Discover", "Components", "Playground", "Learn").forEach { section ->
                        val icon = when (section) {
                            "Discover" -> Icons.Default.Home
                            "Components" -> Icons.Default.Search
                            "Playground" -> Icons.Default.Add
                            else -> Icons.Default.Info
                        }
                        NavigationBarItem(selected = activeSection == section, onClick = {
                            activeSection = section; categoryId = null; demoId = null
                            query = ""
                        }, icon = { Icon(icon, section) }, label = { Text(section) })
                    }
                }
            }
        }) { padding ->
          Row(Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding)) {
            if (useRail) {
              NavigationRail {
                listOf("Discover", "Components", "Playground", "Learn").forEach { section ->
                  val icon = when (section) {
                    "Discover" -> Icons.Default.Home
                    "Components" -> Icons.Default.Search
                    "Playground" -> Icons.Default.Add
                    else -> Icons.Default.Info
                  }
                  NavigationRailItem(selected = activeSection == section, onClick = {
                    activeSection = section; categoryId = null; demoId = null; query = ""
                  }, icon = { Icon(icon, section) }, label = { Text(section) })
                }
              }
            }
            Box(Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.TopCenter) {
              Box(Modifier.widthIn(max = AppSpacing.contentMaxWidth).fillMaxSize()) {
                when {
                  categoryId == null && demoId == null && activeSection == "Discover" -> HomeScreen(
                    onCatalog = { activeSection = "Components" },
                    onLearn = { activeSection = "Learn" },
                    onCounter = { playgroundFeature = "Counter"; activeSection = "Playground" },
                    onCalculator = { playgroundFeature = "Calculator"; activeSection = "Playground" },
                    onFeatured = { selectedCategory, selectedDemo ->
                      activeSection = "Components"; categoryId = selectedCategory; demoId = selectedDemo
                    },
                  )
                  categoryId == null && demoId == null && activeSection == "Playground" ->
                    PlaygroundScreen(playgroundFeature) { playgroundFeature = it }
                  categoryId == null && demoId == null && activeSection == "Learn" -> LearnScreen {
                    activeSection = "Components"; categoryId = "advanced"; demoId = "native-controls"
                  }
                  else -> CatalogScreen(
                    categories = categories,
                    category = category,
                    selectedEntry = demo,
                    searchResults = searchResults,
                    query = query,
                    onQueryChange = { query = it },
                    onCategorySelected = { categoryId = it.id },
                    onEntrySelected = { categoryId = it.categoryId; demoId = it.id; query = "" },
                  )
                }
              }
            }
          }
        }
    }
}
