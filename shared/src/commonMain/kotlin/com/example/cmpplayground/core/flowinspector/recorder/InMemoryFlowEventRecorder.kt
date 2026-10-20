package com.example.cmpplayground.core.flowinspector.recorder

import com.example.cmpplayground.core.flowinspector.model.FlowEvent
import com.example.cmpplayground.core.flowinspector.model.FlowLayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryFlowEventRecorder(
    private val capacity: Int = 100,
    private val clock: () -> Long = { 0L },
) : FlowEventRecorder {
    private val mutableEvents = MutableStateFlow<List<FlowEvent>>(emptyList())
    override val events = mutableEvents.asStateFlow()
    private var nextId = 0L
    private var enabled = true

    init { require(capacity > 0) }

    override fun record(operationId: String, layer: FlowLayer, description: String) {
        if (!enabled) return
        mutableEvents.value = (mutableEvents.value + FlowEvent(
            id = nextId++, operationId = operationId, layer = layer,
            description = description, timestampMillis = clock(),
        )).takeLast(capacity)
    }

    override fun clear() { mutableEvents.value = emptyList() }
    override fun setEnabled(enabled: Boolean) { this.enabled = enabled }
}
