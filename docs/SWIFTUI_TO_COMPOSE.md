# SwiftUI to Compose cheat sheet

| SwiftUI | Compose | Important distinction |
|---|---|---|
| `Text("Hello")` | `Text("Hello")` | Similar rendering intent; modifiers and layout differ. |
| `Button("Save", action: save)` | `Button(onClick = save) { Text("Save") }` | Compose's label is composable content. |
| `VStack` / `HStack` / `ZStack` | `Column` / `Row` / `Box` | Layout policies and measurement are not identical. |
| `List` | `LazyColumn` | Similar lazy vertical presentation, not feature-equivalent. |
| `@State` | `remember { mutableStateOf(...) }` | `remember` is composition-scoped; saveable state has different limits. |
| `@Binding` | State hoisting plus value and callback | Kotlin does not use a property-wrapper binding here. |
| `ObservableObject` / `@Observable` | ViewModel with `StateFlow<UiState>` | Observation mechanics and lifecycle differ. |
| `NavigationStack` | Compose Navigation | Routes/back-stack APIs and destination ownership differ. |
| `Task {}` / `async` | coroutine scope / `suspend` | Cancellation and structured-concurrency semantics differ. |

```swift
struct CounterView: View {
    @State private var count = 0
    var body: some View {
        VStack {
            Text("\\(count)")
            Button("Increment") { count += 1 }
        }
    }
}
```

```kotlin
@Composable
fun CounterView() {
    var count by remember { mutableIntStateOf(0) }
    Column {
        Text(count.toString())
        Button(onClick = { count += 1 }) { Text("Increment") }
    }
}
```

For app-level state, move ownership out of the UI function: send events to a ViewModel and render its immutable state. See the Counter in Playground and the Flow Inspector for a running example.
