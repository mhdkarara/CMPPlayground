package com.example.cmpplayground.core.flowinspector.model

data class FlowEvent(
    val id: Long,
    val operationId: String,
    val layer: FlowLayer,
    val description: String,
    val timestampMillis: Long,
)
