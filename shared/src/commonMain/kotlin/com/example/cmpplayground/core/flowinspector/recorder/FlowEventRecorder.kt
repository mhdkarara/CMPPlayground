package com.example.cmpplayground.core.flowinspector.recorder

import com.example.cmpplayground.core.flowinspector.model.FlowEvent
import com.example.cmpplayground.core.flowinspector.model.FlowLayer
import kotlinx.coroutines.flow.StateFlow

interface FlowEventRecorder {
    val events: StateFlow<List<FlowEvent>>
    fun record(operationId: String, layer: FlowLayer, description: String)
    fun clear()
    fun setEnabled(enabled: Boolean)
}
