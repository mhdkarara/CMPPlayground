# Project Structure

The project uses a feature-first layout inside one shared KMP module. The goal is to make ownership clear without adding extra Gradle modules or layers.

```text
shared/src/
├── commonMain/kotlin/com/example/cmpplayground/
│   ├── App.kt                         # theme + app navigation entry
│   ├── core/
│   │   ├── designsystem/theme/        # Material 3 app theme
│   │   ├── navigation/                # existing saved-state app shell
│   │   ├── codecomparison/            # reusable model and comparison UI
│   │   └── flowinspector/             # reusable event model, recorder, and panel
│   ├── feature/
│   │   ├── home/ui/
│   │   ├── catalog/{model,registry,ui,examples}/
│   │   ├── playground/{ui,counter,calculator}/
│   │   └── learn/ui/
│   └── platform/                      # expect declarations and platform info
├── androidMain/kotlin/.../platform/   # Android actual integrations
├── iosMain/kotlin/.../platform/       # UIKit actual integrations
└── commonTest/kotlin/...              # tests organized alongside ownership
```

## Ownership Rules

- `core/` contains app-wide infrastructure reusable by multiple features. Keep feature-specific behavior out of it.
- `feature/` owns screens, feature state, and feature content. Counter controls belong with Counter; Button snippet strings belong with the catalog Button example.
- `platform/` contains common expect declarations. Android and iOS actual implementations stay in their corresponding source sets.
- `commonTest/` mirrors feature/core package ownership so tests stay close to the behavior they protect.

## Add a Catalog Example

1. Define a `CatalogDemo` value in `feature/catalog/model/CatalogDemo.kt`, including a stable kebab-case ID, title, category ID, and learning goal. `CatalogRegistry` derives browsing entries from these definitions.
2. Write the original preview in the appropriate category file: `EssentialsLessons.kt`, `LayoutLessons.kt`, `InputLessons.kt`, `FeedbackLessons.kt`, or `AdvancedLessons.kt`.
3. Add the enum value to the exhaustive renderer dispatch in `CatalogExampleScreens.kt`. Lessons requiring a full screen have dedicated functions outside the scrolling lesson container.
4. Add focused catalog/search tests if behavior changes, and verify home/learn shortcuts still resolve.
5. For an Essentials comparison, add focused SwiftUI/Compose excerpts in `EssentialsCodeExamples.kt`. Keep snippets aligned with the scenario and explain intentional differences.

The scenarios share a trail-planning theme. Prefer a meaningful interaction and a clear learning goal over a collection of disconnected API calls. `trail_marker.xml` is an original bundled vector used by the image lesson.

## Add a Playground Feature

Place the feature below `feature/playground/<name>/`. Keep immutable UI state and its ViewModel with the feature. Expose StateFlow as read-only and pass actions from the screen to the ViewModel. Use a repository only when the feature has a real data-access boundary that benefits from one.

## Use the Flow Inspector

Inject the shared `FlowEventRecorder` into a feature ViewModel. Record actual actions and state boundaries with an operation ID. Collect the recorder events in the feature route and render them with `core/flowinspector/ui/FlowInspector`. Do not emit pretend recomposition events.

## Navigation

`core/navigation/AppNavigation.kt` currently owns the section and catalog selection state, search, and back behavior. It is not yet a route-based Navigation Compose host even though the multiplatform navigation dependency is configured. Keep feature UI behind callbacks and stable catalog IDs while a later navigation migration is evaluated and tested separately.

## Platform Entry Points

Android enters through `androidApp/.../MainActivity.kt`. iOS enters through `iosApp` and hosts the shared `MainViewController`. Both render the same shared `App()` composable.
