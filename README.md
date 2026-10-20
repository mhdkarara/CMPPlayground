# CMP Playground

An educational Android and iOS app for SwiftUI developers learning Kotlin and Compose Multiplatform. Explore interactive components, compare SwiftUI and Compose code, and follow user actions through shared MVVM state.

Both apps render the same shared Compose UI. SwiftUI snippets are learning material; the iOS app uses a small SwiftUI host to display the shared Compose view controller.

## Contents

- [Who this repository is for](#who-this-repository-is-for)
- [Quick start](#quick-start)
- [Explore the app](#explore-the-app)
- [Component catalog](#component-catalog)
- [Requirements](#requirements)
- [Run Android](#run-android)
- [Run iOS](#run-ios)
- [Tests and verification](#tests-and-verification)
- [Project structure](#project-structure)
- [How the application works](#how-the-application-works)
- [Learn by experimenting](#learn-by-experimenting)
- [Guided exercises](#guided-exercises)
- [MVVM Flow Inspector](#mvvm-flow-inspector)
- [Add or change an example](#add-or-change-an-example)
- [Development workflow](#development-workflow)
- [Troubleshooting](#troubleshooting)
- [Learning resources](#learning-resources)
- [Current scope](#current-scope)
- [Example ownership](#example-ownership)

## Who this repository is for

CMP Playground is a hands-on reference for learning how Kotlin Multiplatform and Compose Multiplatform fit together. It is especially useful for developers who know SwiftUI and want to understand shared declarative UI, Kotlin state management, and native platform integration through working examples.

You can use it in three ways:

- **As a component reference:** browse a category, interact with a control, and find its implementation in the catalog renderer.
- **As an architecture example:** follow Counter or Calculator from a button tap through a ViewModel and observable state back to the screen.
- **As a learning sandbox:** change a layout, add a calculator action, or implement a native integration and compare the result on Android and iOS.

Basic familiarity with declarative UI helps. You do not need to know every Kotlin or Compose API before starting; the small Counter feature is a useful first code-reading exercise. Developing the Android app requires an Android toolchain; developing the iOS host also requires macOS and Xcode.

The repository contains a local learning application. The implemented examples do not require a backend account or API key. Calculator history and inspector events are held in memory, so they are not a storage or synchronization example.

## Quick start

Clone or download this repository, then open its **Gradle project root** in Android Studio: the directory containing this README, `settings.gradle.kts`, `gradlew`, and the application modules.

After installing the [requirements](#requirements), run this from the Gradle project root:

```sh
./gradlew :androidApp:assembleDebug
```

To use the app, run the `androidApp` configuration in Android Studio, or follow [Run iOS](#run-ios) in Xcode. Start with **Components → Essentials → Buttons: save a route** to explore a live example and its code comparisons.

## Explore the app

| Section | What you can try |
| --- | --- |
| Discover | Featured examples and shortcuts to the playgrounds. |
| Components | Search 43 examples across Essentials, Layout & containers, Forms & input, Feedback & overlays, Motion, and Advanced & platform. |
| Playground | Counter and calculator demos backed by ViewModels and observable state. |
| Learn | Short Swift-to-Kotlin and SwiftUI-to-Compose mappings, plus a shared-versus-platform demo. |

The ten Essentials examples include SwiftUI, Compose, and Compare code panels with selectable text and clipboard support. The route-saving lesson includes filled, outlined, and text actions with state-dependent availability. The platform comparison demonstrates shared controls alongside native Android and iOS switches, keyboard input, and haptic requests. Navigation uses a bottom bar on compact screens and a rail on wider screens.

## Component catalog

The catalog registers 43 examples in six categories. Search matches display titles and category names, ignores case, and requires every word in the query to match somewhere in that combined text. For example, `buttons save route` finds the route-saving lesson, and `forms radio pace` finds the walking-pace lesson.

| Category | Entries | Examples and learning focus |
| --- | --- | --- |
| Essentials | 10 | Trail journal typography, our trail marker image, route-saving buttons, simulated sync, packing card, readiness surface, itinerary divider, adjustable team marker shape, waypoints, and update badge. |
| Layout & containers | 10 | Trip timeline, gear row, route overlay, available-space inspection, guide profile reflow, trail carousel, itinerary list, supply grid, trip scaffold, and padding experiment. |
| Forms & input | 8 | Trail name, stop search, multiline trip notes, gear checklist, walking pace, offline preference, distance target, and region menu. |
| Feedback & overlays | 6 | Discard confirmation, packing progress, cancellable simulated download, route-details sheet, trip-folder drawer, and undoable stop removal. |
| Motion | 1 | A weather forecast that crossfades between clear and rainy states. |
| Advanced & platform | 8 | Elevation sketch, lifecycle event log, local editor back handling, measured labels, route toolbar, planner rail, shared/native controls, and MVVM calculator. |

To explore an entry, open **Components**, search or select a category, then open an example and interact with its preview. Code comparison panels are currently available for the ten Essentials examples; the rest of the catalog can be studied in the Kotlin source.

In a code comparison, select **SwiftUI**, **Compose**, or **Compare**. Compare stacks the panels on compact screens and places them side by side when space permits. Use **Copy** to copy a complete snippet, or select part of the text. Read the explanation and differences below the snippets before translating a pattern into your own app.

Snippet strings are educational content. Editing a snippet changes the displayed code; changing the live demo requires editing its composable implementation as well.

## Requirements

The application project is in the **Gradle project root**, alongside this README. Open that directory in Android Studio and run Gradle commands from there.

- Android Studio with Kotlin Multiplatform support for editing and running Android.
- JDK 21: the checked-in Gradle daemon criteria request Azul JDK 21. Android bytecode targets Java 11, separately from the Gradle runtime.
- Android SDK Platform 37 and an emulator or device running API 24 or later. The app compiles and targets API 37.
- For iOS: macOS, Xcode with an SDK supporting the configured iOS 18.2 deployment target, and an Apple Silicon simulator or physical iOS device. The shared module configures `iosSimulatorArm64` and `iosArm64`; an Intel simulator target is not configured.
- Network access on the first build to download Gradle, dependencies, and any required toolchain.

The project pins Gradle 9.5.1, Android Gradle Plugin 9.1.1, Kotlin 2.4.20, Compose Multiplatform 1.12.1, and Material 3 1.12.0-alpha03. See the [version catalog](gradle/libs.versions.toml) for dependency configuration.

Use the checked-in Gradle wrapper rather than a separately installed Gradle version. Desktop, web, and Intel iOS simulator targets are not configured in this project.

## Run Android

1. Open the directory containing `settings.gradle.kts` in Android Studio and let Gradle sync.
2. Configure the Android SDK in the IDE. For command-line builds, set `ANDROID_HOME` or add `sdk.dir=/absolute/path/to/Android/sdk` to `local.properties` using your local SDK path.
3. Select the `androidApp` run configuration and an emulator or connected device, then run.

To build from the repository root:

```sh
./gradlew :androidApp:assembleDebug
```

The debug APK is written to `androidApp/build/outputs/apk/debug/androidApp-debug.apk`, relative to the application directory. With an emulator running or device connected, install it with:

```sh
./gradlew :androidApp:installDebug
```

Launch **CMPPlayground** on the device. On Windows, use `gradlew.bat` in place of `./gradlew`.

## Run iOS

1. Open `iosApp/iosApp.xcodeproj` in Xcode.
2. Select the `iosApp` scheme and a simulator running iOS 18.2 or later, then run.
3. For a physical device, configure your development team through `TEAM_ID` in [Config.xcconfig](iosApp/Configuration/Config.xcconfig) and ensure the bundle identifier and signing settings match your account.

The Xcode build phase runs `:shared:embedAndSignAppleFrameworkForXcode` to build and embed the static `Shared` framework. The native host is `iosApp/iosApp/ContentView.swift`; the Kotlin entry point is `shared/src/iosMain/kotlin/com/example/cmpplayground/MainViewController.kt`.

To check simulator Kotlin compilation independently, run this from the application directory:

```sh
./gradlew :shared:compileKotlinIosSimulatorArm64
```

Compilation alone does not verify the Xcode app, signing, or simulator behavior.

## Tests and verification

From the Gradle project root, run the shared test suite:

```sh
./gradlew :shared:allTests
```

The aggregate task covers configured test targets; iOS tests require macOS, Xcode, and a compatible simulator. Inspect the tasks available on your machine with:

```sh
./gradlew :shared:tasks --all
```

Common tests cover catalog registration and search, Essentials comparison content, calculator behavior, Counter state, and Flow Inspector recording. Android host and iOS source sets also contain basic smoke tests. Reports are generated under each module's `build/` directory.

For platform-specific test runs, use:

```sh
# Run common tests together with Android host tests.
./gradlew :shared:testAndroidHostTest

# Run common tests together with iOS simulator tests on a configured Mac.
./gradlew :shared:iosSimulatorArm64Test
```

To focus on calculator model tests during development:

```sh
./gradlew :shared:testAndroidHostTest --tests '*CalculatorModelTest'
```

These commands serve different purposes. A model test checks behavior without rendering the app; Kotlin compilation checks source compatibility; an APK build checks Android packaging; running the app on a device or simulator checks interaction and native behavior. A passing test suite alone does not establish that the UI behaves correctly on both platforms.

See the [progress log](PROGRESS.md) for build and test results. Device/simulator interaction and screenshot verification are tracked separately; screenshots are not yet included.

## Project structure

```text
.                                  # Gradle project root
├── README.md
├── androidApp/                    # Android application and MainActivity
├── iosApp/                        # Xcode project and SwiftUI host
├── shared/src/
│   ├── commonMain/
│   │   ├── composeResources/      # Bundled trail marker vector
│   │   └── kotlin/com/example/cmpplayground/
│   │       ├── App.kt             # Shared theme/navigation entry
│   │       ├── core/              # Design system, navigation, comparison, inspector
│   │       ├── feature/           # Home, catalog, playground, learn
│   │       └── platform/          # Shared expect declarations
│   ├── androidMain/               # Android actual implementations
│   ├── iosMain/                   # UIKit entry and iOS actual implementations
│   ├── commonTest/                # Portable feature and core tests
│   ├── androidHostTest/            # Android host smoke test
│   └── iosTest/                    # iOS smoke test
├── docs/                          # Architecture and learning references
└── gradle/                        # Wrapper, JVM criteria, version catalog
```

Shared code uses a feature-first layout within one Kotlin Multiplatform module. Feature screens send actions to ViewModels and observe read-only `StateFlow` UI state. Platform boundaries use `expect`/`actual` for native integration such as switches and clipboard access.

Navigation currently uses saved section and catalog selection state with platform back handling. Navigation Compose is configured as a dependency; the app does not yet use a route-based `NavHost`.

See [Project Structure](docs/PROJECT_STRUCTURE.md) for ownership rules and instructions for extending the app, and [Architecture](docs/ARCHITECTURE.md) for state and lifecycle details.

## How the application works

### Application entry points

Android launches `MainActivity` in `androidApp`, which hosts the shared Compose application. On iOS, `ContentView.swift` wraps the Kotlin `MainViewController()` in `UIViewControllerRepresentable`. That controller hosts the same shared `App()` composable.

```text
Android Activity                    iOS SwiftUI host
       |                                   |
       |                         Kotlin MainViewController
       |                                   |
       +---------------- App() ------------+
                           |
                        AppTheme
                           |
                     AppNavigation
                           |
          Discover / Components / Playground / Learn
```

The SwiftUI host handles embedding; most application UI lives in Kotlin. Shared Compose controls use the app's Material theme on both platforms. A Compose switch on iOS therefore illustrates shared rendering; the separate native switch illustrates embedding a host control.

### State and MVVM

Counter and Calculator separate state ownership from rendering. The ViewModel holds a private `MutableStateFlow` and exposes read-only state. The route obtains the ViewModel through `viewModel()` and observes it using `collectAsStateWithLifecycle()`.

```text
User taps a control
        |
        v
Screen forwards an action
        |
        v
ViewModel handles the action
        |
        v
Model computes the next immutable state
        |
        v
StateFlow publishes state -> screen observes it -> UI updates
```

The calculator keeps its calculation rules in `CalculatorModel.reduce(state, action)`. This makes operations, error handling, and history behavior testable without Android or UIKit. Counter uses a smaller state transformation directly in its ViewModel because it does not need a separate domain layer.

ViewModels belong to the current host's `ViewModelStoreOwner`. Their lifetime follows that owner rather than every individual screen render. In-memory ViewModel state is not durable storage, and neither calculator history nor inspector events are restored from disk after process termination.

### Shared and platform-specific code

| Responsibility | Shared implementation | Native responsibility |
| --- | --- | --- |
| Feature layouts | Compose screens in `commonMain` | Host the shared UI. |
| Calculator operations and history | Immutable model and ViewModel | No separate native calculator implementation. |
| Native switch | `expect` composable and shared state | Android `Switch` or iOS `UISwitch` through `actual` implementations. |
| Clipboard | Shared copy callback boundary | Android and iOS clipboard APIs. |
| Keyboard and haptic requests | Compose API calls | OS keyboard presentation and supported haptic response. |
| Back handling | Shared catalog selection and callbacks | Android system-back integration and iOS-specific handling. |

`expect`/`actual` connects a common declaration to a target implementation at compilation time. Keep ordinary feature behavior in shared code and introduce a platform boundary when the feature needs an OS API or native view.

## Learn by experimenting

1. Open **Components → Essentials → Buttons: save a route**. Try the controls, then switch between SwiftUI, Compose, and Compare panels.
2. Open **Playground → Counter**. Change the value and follow the action through the Flow Inspector.
3. Switch to **Calculator** and inspect how a key action reaches the ViewModel, publishes state, and returns to the screen.
4. Open **Learn → Shared vs platform code** on each platform and compare the Compose and native switches.

| Familiar SwiftUI concept | Compose starting point |
| --- | --- |
| `VStack`, `HStack`, `ZStack` | `Column`, `Row`, `Box` |
| Local `@State` | `remember { mutableStateOf(...) }` or `rememberSaveable` for supported restorable state |
| Observable screen state | ViewModel exposing `StateFlow<UiState>` |
| View modifiers | Ordered `Modifier` chains and composable parameters |

These are conceptual comparisons; lifecycle, identity, state restoration, and API behavior differ. Read the [SwiftUI-to-Compose reference](docs/SWIFTUI_TO_COMPOSE.md) for more detail. The [learning guide](CMP_LEARNING_GUIDE.md) walks through the original catalog scenarios, shared/native controls, and calculator.

## Guided exercises

### 1. Follow a Counter action

Open **Playground → Counter**. Tap **Increment**, use **−**, then **Reset**. Expand **Explore MVVM Flow** to inspect recorded events. Tap an event to reveal its operation ID and event number, then compare the UI, ViewModel, state, and observation boundaries.

Read these files in order:

1. [CounterScreen.kt](shared/src/commonMain/kotlin/com/example/cmpplayground/feature/playground/counter/CounterScreen.kt): lifecycle collection, action callbacks, and the inspector panel.
2. [CounterViewModel.kt](shared/src/commonMain/kotlin/com/example/cmpplayground/feature/playground/counter/CounterViewModel.kt): immutable state and increment/decrement/reset transformations.
3. [CounterViewModelTest.kt](shared/src/commonTest/kotlin/com/example/cmpplayground/feature/playground/counter/CounterViewModelTest.kt): behavior checks independent of the UI.

As an exercise, add an action that increments by five. Update the ViewModel, expose the action through the screen, and check the resulting count in a focused test. This follows the existing feature without introducing native code.

### 2. Explore Calculator rules

Open **Playground → Calculator** and try these sequences:

| Input sequence | Expected behavior |
| --- | --- |
| `0.1 + 0.2 =` | Displays `0.3`. |
| `2 + 3 * 4 =` | Displays `20`; operations execute immediately. |
| `2 + 3 = =` | Displays `8` after the second equals action. |
| `1 / 0 =` | Shows an error; entering a digit or using AC starts over. |
| `50 %` | Displays `0.5`. |
| Tap a history item | Recalls its result as a fresh operand. |

The calculator supports the four basic operations, decimal input, sign changes, percent, backspace, repeated equals, and history recall. Percent divides the current operand by 100. AC clears the active calculation while preserving history; the history clear control removes history separately.

Calculation uses `Double`, with display rounding to 12 significant digits and a limit of 12 typed digits. History retains the latest 20 results in memory. It is an immediate-execution learning calculator rather than an expression parser with operator precedence.

Read [CalculatorModel.kt](shared/src/commonMain/kotlin/com/example/cmpplayground/feature/playground/calculator/CalculatorModel.kt), then [CalculatorViewModel.kt](shared/src/commonMain/kotlin/com/example/cmpplayground/feature/playground/calculator/CalculatorViewModel.kt), then [CalculatorScreen.kt](shared/src/commonMain/kotlin/com/example/cmpplayground/feature/playground/calculator/CalculatorScreen.kt). Follow one action through the reducer and compare it with the inspector output.

### 3. Compare shared and native controls

Open **Learn → Shared vs platform code** on Android and iOS. Toggle the Compose switch, then the native switch. Both switches on one screen read and update the same Boolean state, so either interaction should update both controls. Separate Android and iOS app instances do not synchronize with each other.

Switch the input hint between Text, Email, and Number and focus the field. The shared UI requests an input type, while the OS supplies the keyboard. The hint does not validate pasted or typed content. Keyboard appearance also depends on locale, OS version, and installed keyboards.

Try the haptic control on physical devices. Its count represents requests; actual feedback depends on hardware and system settings. Compare the native switch implementations in `shared/src/androidMain/.../platform/` and `shared/src/iosMain/.../platform/` to see where platform APIs enter the application.

### 4. Change a layout on both platforms

Choose **Column: trip timeline**, **Row: gear summary**, or **Modifier padding: route label**. Find its rendering branch in `LayoutLessons.kt`, change spacing or alignment, then run both hosts. Observe how a shared layout change reaches both platforms and how screen width affects the surrounding navigation and code panels.

## MVVM Flow Inspector

The Counter and calculator include a bounded in-memory event log. It records explicit UI actions, ViewModel handling, state publication, and screen collection events, grouped by operation. The recorder retains the latest 100 events by default and supports clearing and enabling/disabling recording.

The inspector records application boundaries rather than introspecting the Compose runtime. A collection event does not prove that a particular child composable recomposed. Events stay in memory; the recorder does not transmit analytics or record user-entered content.

To use the panel:

1. Expand **Explore MVVM Flow** in Counter or Calculator.
2. Perform an action and look for events sharing its operation ID.
3. Tap an event to show additional identifiers.
4. Turn off **Record** to pause event recording while continuing to use the feature.
5. Use **Clear** to remove recorded events without resetting the feature's state.

The panel displays the latest 12 events, while the default recorder retains up to 100. Recorded state-update descriptions reflect explicit instrumentation; assigning an equal StateFlow value may not produce a new UI observation. Do not interpret the panel as an exact count of emissions, rendered frames, or recompositions.

## Add or change an example

For a catalog component:

1. Add a lesson to [CatalogDemo.kt](shared/src/commonMain/kotlin/com/example/cmpplayground/feature/catalog/model/CatalogDemo.kt) with a stable kebab-case ID, title, category, and learning goal. The registry derives category entries from these typed definitions.
2. Implement an original scenario in the relevant category file (`EssentialsLessons.kt`, `LayoutLessons.kt`, `InputLessons.kt`, `FeedbackLessons.kt`, or `AdvancedLessons.kt`). Connect the enum value in the exhaustive dispatch in [CatalogExampleScreens.kt](shared/src/commonMain/kotlin/com/example/cmpplayground/feature/catalog/examples/CatalogExampleScreens.kt).
3. If the example includes source comparisons, define its `CodeExample` content alongside the feature and display it with the shared `CodeComparison` composable. Existing Essentials and Button examples provide starting points.
4. Update relevant catalog/search tests in `shared/src/commonTest/` and try the example on Android and iOS.

For a playground, create a package under `shared/src/commonMain/kotlin/com/example/cmpplayground/feature/playground/<name>/`. Follow Counter or Calculator: keep immutable UI state and its ViewModel together, expose read-only `StateFlow`, and send screen actions through ViewModel methods. Wire the feature into `PlaygroundScreen.kt`.

Keep portable UI and behavior in `commonMain`. Put native API integrations in `androidMain` and `iosMain`, with shared `expect` declarations where needed. See [Project Structure](docs/PROJECT_STRUCTURE.md) for ownership rules and [Architecture](docs/ARCHITECTURE.md) for state management details.

Before submitting a change, build Android, run the relevant shared tests, and check iOS compilation when shared or iOS code changes. Include the commands you ran and any platform checks still pending.

## Development workflow

Start from the feature you want to change, then identify whether the change belongs in its rendering, state handling, model, or a platform integration. Most component and calculator changes can stay within `shared`.

| Change | Starting location | Useful verification |
| --- | --- | --- |
| Catalog title, grouping, or search | `feature/catalog/registry/` | Catalog tests and a search in the running app. |
| Interactive component | `feature/catalog/examples/` | Android build and visual interaction on both hosts. |
| Calculator rule | `feature/playground/calculator/CalculatorModel.kt` | Calculator model tests, then a manual input sequence. |
| Counter behavior | `feature/playground/counter/` | ViewModel tests and inspector observation. |
| App colors or spacing | `core/designsystem/` | Compact and wide screen checks. |
| Clipboard or native switch | `platform/` across source sets | Target compilation and native interaction on each platform. |
| Section selection or back behavior | `core/navigation/` and platform back handlers | Navigate into an example, return, and verify selection on each host. |

From the Gradle project root, a typical verification sequence for shared code is:

```sh
./gradlew :shared:testAndroidHostTest
./gradlew :androidApp:assembleDebug

# On macOS with Xcode configured:
./gradlew :shared:compileKotlinIosSimulatorArm64
```

Use simulator tests as well when changing shared behavior that needs iOS verification. To inspect a failing Gradle task, rerun that task with `--stacktrace`; use `--info` if you need more build detail. Resolve the first relevant error before retrying broader checks.

For manual review, try the changed feature on a compact phone and a wider window or tablet where available. Check scrolling, code panel readability, keyboard behavior, and back navigation. For native integrations, use a physical device when the simulator cannot represent the behavior, such as haptics.

When documenting a change, state what behavior changed, how to reproduce it, which commands passed, and which device or simulator you used. Keep machine-specific SDK paths, signing credentials, and generated build output out of commits.

## Troubleshooting

| Problem | What to check |
| --- | --- |
| `./gradlew` is missing, or the IDE does not find the modules | Open or change into the directory containing `settings.gradle.kts` and `gradlew`. |
| `Permission denied` when starting the wrapper on macOS/Linux | From the Gradle project root, run `chmod +x gradlew`, then retry. |
| Android SDK location is missing | Configure the SDK in Android Studio, set `ANDROID_HOME`, or add your machine's `sdk.dir` to `local.properties`. Keep that file local. |
| Android platform 37 is missing | Install SDK Platform 37 through Android Studio's SDK Manager and sync again. |
| Gradle cannot find or download its JVM | Check the Azul JDK 21 criteria in `gradle/gradle-daemon-jvm.properties` and network access to its configured toolchain downloads. The Java 11 bytecode target is separate from this runtime requirement. |
| The first build cannot download Gradle or dependencies | Check connectivity, proxy settings, and access to the repositories declared in `settings.gradle.kts`; retry once access is restored. |
| Xcode reports signing or provisioning errors | Set your development team and a valid bundle identifier in `iosApp/Configuration/Config.xcconfig`, then verify Signing & Capabilities in Xcode. |
| The iOS simulator destination is unavailable | Install a simulator runtime compatible with the iOS 18.2 deployment target and use an Apple Silicon simulator. Only `iosSimulatorArm64` is configured. |
| Xcode cannot find the shared framework | Build the app through the `iosApp` scheme so its Gradle build phase creates and embeds `Shared`; inspect that phase's output for the underlying Kotlin/Gradle error. |

## Learning resources

- [Architecture](docs/ARCHITECTURE.md): shared state, ViewModels, lifecycle, and the Flow Inspector.
- [Project Structure](docs/PROJECT_STRUCTURE.md): package ownership, entry points, and extension steps.
- [SwiftUI to Compose](docs/SWIFTUI_TO_COMPOSE.md): conceptual mappings for SwiftUI developers.
- [CMP Learning Guide](CMP_LEARNING_GUIDE.md): original scenario exercises, platform integration, and calculator walkthroughs.
- [Progress log](PROGRESS.md): completed work, remaining features, and verification status.

## Current scope

This is an active learning project. Remaining work includes route-based navigation, Todo/Products/Settings playgrounds, expanded lessons, state restoration and navigation exercises, and accessibility and runtime verification. See [PROGRESS.md](PROGRESS.md) for tracked implementation status.

## Example ownership

The component catalog uses original trail-planning scenarios written for CMP Playground. The previews, lesson identifiers, SwiftUI/Compose comparison excerpts, and bundled `trail_marker.xml` illustration are maintained in this repository. Code comparisons are focused learning excerpts; they are not complete standalone applications.

Frameworks, libraries, and generated tooling retain their own licenses. In particular, keep the Gradle wrapper's license headers when maintaining the build scripts.
