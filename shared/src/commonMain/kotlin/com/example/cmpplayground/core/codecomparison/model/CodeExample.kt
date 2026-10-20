package com.example.cmpplayground.core.codecomparison.model

data class CodeExample(
    val title: String,
    val description: String,
    val swiftCode: String,
    val kotlinCode: String,
    val explanation: String,
    val keyDifferences: List<String>,
)
