# Learning with CMP Playground

Use the app's original trail-planning scenarios to explore declarative UI, state ownership, and platform boundaries. Android and iOS host the same Compose application; the SwiftUI snippets explain comparable concepts rather than providing a separate native application.

## Start with the component catalog

Open **Components** and choose a category. Each lesson describes a practical task, such as saving a route, packing supplies, or choosing a walking pace. The ten Essentials lessons include SwiftUI and Compose excerpts with explanations. Other categories expose their implementation in the category-owned Kotlin files.

| Category | Suggested starting lesson | What to observe |
| --- | --- | --- |
| Essentials | Buttons: save a route | One Boolean drives status text and enabled states. |
| Layout & containers | Constraints: available space | The available width determines whether the summary uses one or two containers. |
| Forms & input | Checkbox: gear checklist | Three independent choices produce a derived packed count. |
| Feedback & overlays | Snackbar: undo removal | A removal changes state immediately; the snackbar action restores it. |
| Motion | Crossfade: weather outlook | The target state chooses the content; animation transitions between values. |
| Advanced & platform | Canvas: elevation sketch | A slider moves a checkpoint along a piecewise linear profile. |

Find definitions in `shared/src/commonMain/kotlin/com/example/cmpplayground/feature/catalog/model/CatalogDemo.kt`. The renderer in `feature/catalog/examples/CatalogExampleScreens.kt` connects typed lessons to category implementations. IDs use kebab case and are also used by saved selections and home-screen shortcuts.

## Exercise 1: one value, several UI effects

Open **Components → Essentials → Buttons: save a route**.

1. Observe the initial draft status. Save is enabled and Undo save is disabled.
2. Save the route. Observe the status and both enabled states change together.
3. Undo the save, then use Start over.
4. Compare the SwiftUI and Compose excerpts.

Read `EssentialsLessons.kt` and `material/ButtonCodeExamples.kt`. Both the live controls and snippets express the same scenario. The live preview stores local UI state; no save request reaches disk or a server.

As an extension, introduce a confirmation message after saving. Derive visibility from saved state or deliberately model a separate transient message, and explain which choice matches the behavior you want. Update both snippets if you change the lesson's teaching point.

## Exercise 2: understand a bounded scrolling viewport

Open **Components → Layout & containers → Lazy column: itinerary**. Select a stop near the beginning, scroll farther down, and select another. Compare it with **Lazy row: suggested trails** and **Lazy grid: supply shelf**.

Read `LayoutLessons.kt`. The lazy column and grid have a fixed preview height because they live inside an outer scrolling lesson. Giving a vertically scrolling lazy container unbounded height inside another vertical scroller can fail during measurement.

The preview stores the selected index. Its items have stable positional keys because these generated collections do not reorder. If you change the example to support insertions or reordering, introduce stable item IDs and store selection by ID.

## Exercise 3: asynchronous UI feedback

Open **Components → Feedback & overlays → Linear progress: map download**.

1. Start a simulated download.
2. Cancel before completion and observe that progress stops.
3. Start again and observe that progress resets.
4. Leave the lesson while it is running.

Read the `LaunchedEffect(running)` block in `FeedbackLessons.kt`. The delay is local demonstration work. The composition owns this coroutine and cancels it when the effect is removed or its key changes. `rememberSaveable` values may survive supported host restoration, so returning to a lesson can recreate an effect from saved running state; this is not a persistent background transfer.

For a product download, move operation ownership to a ViewModel or service according to the required lifetime. Model idle, running, completed, failed, and cancelled outcomes explicitly rather than treating a timer as a network operation.

## Exercise 4: follow Counter through MVVM

Open **Playground → Counter** and expand **Explore MVVM Flow**.

```text
Tap Increment
    -> UI records the action
    -> CounterViewModel transforms CounterUiState
    -> StateFlow holds the new value
    -> the screen observes state
```

Read `feature/playground/counter/CounterScreen.kt`, `CounterViewModel.kt`, and the corresponding `CounterViewModelTest.kt` in `commonTest`.

Each recorded operation has an ID. Tap an event to reveal its details, pause Record to stop logging, and use Clear to empty the event list. Clear does not reset Counter state. The panel shows 12 recent events from a recorder that retains 100 by default.

Recorded state-update descriptions reflect instrumentation, not a guarantee of a distinct StateFlow emission. For example, resetting an already-zero count may record handling without causing a new observed state. The inspector does not trace individual composable recompositions.

## Exercise 5: isolate calculator behavior

Open **Playground → Calculator**. Read these files in `feature/playground/calculator/`:

| File | Responsibility |
| --- | --- |
| `CalculatorModel.kt` | Immutable state, typed actions, calculation rules, and history. |
| `CalculatorViewModel.kt` | Read-only observable state, action handling, and event recording. |
| `CalculatorScreen.kt` | Lifecycle collection, rendering, callbacks, and inspector controls. |

Try `2 + 3 * 4 =`. The result is `20` because this calculator executes immediately. Try `2 + 3 = =`; repeated equals yields `8`. Try `1 / 0 =`, then type a digit to recover from the error.

The model uses Double and display rounding to 12 significant digits. Input is limited to 12 typed digits. Percent divides the current operand by 100. History retains 20 results in memory; AC clears the active calculation while the separate history control clears past results.

Add a focused model test before changing a calculation rule. Keeping that rule in the reducer allows verification without an Android Activity or UIKit controller.

## Exercise 6: locate the native boundary

Open **Learn → Shared vs platform code** on each platform.

- Toggle either switch. The Compose and native controls within the same screen share one Boolean value.
- Compare appearance: the Compose switch follows the shared Material theme, while the native switch uses its host control.
- Choose Text, Email, and Number and focus the field. The OS provides a keyboard from the shared input hint; the hint does not validate content.
- Request haptic feedback on physical devices. The count records requests, while hardware and settings determine actual feedback.

Read these files under `shared/src/`:

1. `commonMain/.../feature/learn/ui/PlatformComparisonScreen.kt`
2. `commonMain/.../platform/NativeSwitch.kt`
3. `androidMain/.../platform/NativeSwitch.android.kt`
4. `iosMain/.../platform/NativeSwitch.ios.kt`

The native switch boundary uses `expect`/`actual`: common code declares the contract, and each target supplies its implementation at compile time. Separate Android and iOS app instances do not synchronize state with each other.

## Run the checks

Run commands from the directory containing `gradlew` and `settings.gradle.kts`:

```sh
./gradlew :shared:testAndroidHostTest
./gradlew :androidApp:assembleDebug

# On macOS with Xcode configured:
./gradlew :shared:compileKotlinIosSimulatorArm64
./gradlew :shared:iosSimulatorArm64Test
```

Host tests, target compilation, and device interaction check different things. See [README.md](README.md) for setup, [Architecture](docs/ARCHITECTURE.md) for ownership and lifecycle, and [PROGRESS.md](PROGRESS.md) for verification status.
