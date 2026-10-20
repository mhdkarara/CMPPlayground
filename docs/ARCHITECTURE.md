# Architecture

The app shares its Compose UI and feature logic from `shared/src/commonMain`. `App.kt` applies `AppTheme` and delegates to `core/navigation/AppNavigation.kt`. Android and iOS provide platform entry points and selected native integrations.

## UI state and events

```text
Compose screen -- user action --> ViewModel --> Repository / model
     ^                                |
     |                                v
     +---- collect StateFlow<UiState> -+
```

The Counter and calculator demonstrate this loop. Their screens render immutable state, send actions to ViewModels, and collect read-only `StateFlow`. State changes can trigger Compose recomposition; the inspector records when the screen collector processes a value, not which composables recomposed.

## Ownership and lifecycle

`viewModel()` owns feature ViewModels at the current Compose owner. AndroidX Lifecycle provides the multiplatform ViewModel API; `collectAsStateWithLifecycle` adapts collection to the owner lifecycle. The app currently uses saved section/selection state and platform back handlers, not a Navigation Compose `NavHost`. The dependency is present, but a route migration is intentionally separate from these structural moves. SwiftUI `@StateObject` and a shared ViewModel are useful comparisons, but their ownership and lifecycle rules are not interchangeable.

## Sourcesets

- `commonMain`: Compose UI, feature state, examples, and platform-independent logic.
- `androidMain`: Android entry point and Android integrations.
- `iosMain`: UIKit-backed iOS entry point and iOS integrations.
- `commonTest`: portable feature and model tests.

The project currently has a shared Android library and iOS framework. Platform-only code should be introduced only for functionality that genuinely needs the host API.

## MVVM Flow Inspector

The reusable recorder and panel are in `core/flowinspector/`. The in-memory educational event log has bounded capacity. Events are written at explicit UI, ViewModel, StateFlow-update, and UI-collection boundaries. It does not inspect Compose internals, infer recomposition, transmit data, or capture user-entered content.

## Code comparisons

Reusable comparison rendering and the `CodeExample` model are in `core/codecomparison/`. Specific Button source and explanation content are owned by `feature/catalog/examples/material/`, keeping feature content out of the renderer.

## Catalog

Catalog metadata and search are in `feature/catalog/model/` and `feature/catalog/registry/`. Typed `CatalogDemo` definitions own stable IDs, titles, categories, and learning goals. `CatalogExampleScreens.kt` dispatches to original trail-planning lessons in category-owned files with an exhaustive enum switch. Full-screen lessons own their scaffold, drawer, or rail; scrolling lessons share a heading and comparison container. `feature/catalog/ui/` owns browsing and row presentation.
