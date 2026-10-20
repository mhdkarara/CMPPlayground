# CMP Playground progress

Last updated: 2026-10-20

## Implemented

- Shared Android/iOS Compose app with Discover, Components, Playground, and Learn sections.
- Feature-owned screens, centralized theme, reusable code comparison, and bounded Flow Inspector.
- An original catalog of 43 trail-planning lessons across six learning categories.
- Typed `CatalogDemo` definitions with stable IDs, titles, categories, and learning goals; catalog browsing derives from these definitions.
- Exhaustive lesson dispatch and category-owned Essentials, Layout, Input, Feedback, and Advanced renderer files.
- Original SwiftUI/Compose comparison excerpts for all ten Essentials lessons.
- An original bundled `trail_marker.xml` vector for the image lesson.
- Updated Discover and Learn shortcuts using the new IDs.
- Shared Counter and calculator state, ViewModels, and behavior tests.
- Native switch and clipboard integrations through platform implementations.
- Updated README, learning exercises, architecture, and extension guidance.

## Verification

The following checks completed successfully after rebuilding the catalog:

```sh
./gradlew :shared:testAndroidHostTest :androidApp:assembleDebug :shared:compileKotlinIosSimulatorArm64 --console=plain
```

- Android host tests, including shared catalog, code comparison, calculator, Counter, and recorder checks, passed.
- Android debug APK assembled successfully.
- Kotlin compilation for the Apple Silicon iOS simulator target passed.
- Catalog tests check ID uniqueness, category ownership, search, shortcut resolution, and Essentials comparison coverage.
- Full Xcode app build, iOS simulator test execution, and device/simulator UI interaction have not been verified in this change.

## Remaining work

- Consider route-based navigation; the current app uses saved section and catalog selection state.
- Add Todo, asynchronous Products, and Settings playgrounds if they support the learning goals.
- Expand Kotlin lessons, state-restoration comparisons, and navigation exercises.
- Verify screenshots, compact/wide layouts, accessibility, keyboard behavior, and native interactions on running Android and iOS apps.
- Validate physical-device haptics and native control behavior separately from compilation.
