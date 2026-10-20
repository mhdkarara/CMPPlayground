package com.example.cmpplayground.feature.catalog.examples.material

import com.example.cmpplayground.core.codecomparison.model.CodeExample

object ButtonCodeExamples {
    val comparison = CodeExample(
        title = "Save a route",
        description = "One saved value drives the status and availability of three route actions.",
        swiftCode = """@State private var saved = false

VStack(alignment: .leading) {
    Text(saved ? "Cedar loop is saved" : "Cedar loop is a draft")
    Button("Save route") { saved = true }
        .buttonStyle(.borderedProminent)
        .disabled(saved)
    Button("Undo save") { saved = false }
        .buttonStyle(.bordered)
        .disabled(!saved)
    Button("Start over") { saved = false }
}""",
        kotlinCode = """var saved by rememberSaveable { mutableStateOf(false) }

Column {
    Text(if (saved) "Cedar loop is saved" else "Cedar loop is a draft")
    Button(onClick = { saved = true }, enabled = !saved) { Text("Save route") }
    OutlinedButton(onClick = { saved = false }, enabled = saved) { Text("Undo save") }
    TextButton(onClick = { saved = false }) { Text("Start over") }
}""",
        explanation = "This preview owns a local flag, with no storage. A product save action would be forwarded to a ViewModel or another state owner.",
        keyDifferences = listOf(
            "SwiftUI styles Button; Compose chooses Button, OutlinedButton, or TextButton.",
            "SwiftUI disabled is the inverse of Compose enabled.",
            "rememberSaveable supports host state restoration, not durable disk persistence.",
        ),
    )
}
