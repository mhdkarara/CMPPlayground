package com.example.cmpplayground.feature.playground.counter

import androidx.lifecycle.ViewModel
import com.example.cmpplayground.core.flowinspector.model.FlowLayer
import com.example.cmpplayground.core.flowinspector.recorder.FlowEventRecorder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CounterUiState(val count: Int = 0)

class CounterViewModel(
    val recorder: FlowEventRecorder,
) : ViewModel() {
    private val mutableState = MutableStateFlow(CounterUiState())
    val state = mutableState.asStateFlow()

    fun increment(operationId: String) = change(operationId, "Increment") { it + 1 }
    fun decrement(operationId: String) = change(operationId, "Decrement") { it - 1 }
    fun reset(operationId: String) = change(operationId, "Reset") { 0 }

    private fun change(operationId: String, action: String, transform: (Int) -> Int) {
        recorder.record(operationId, FlowLayer.VIEW_MODEL, "CounterViewModel.$action invoked")
        mutableState.update { it.copy(count = transform(it.count)) }
        recorder.record(operationId, FlowLayer.STATE_FLOW, "CounterUiState(count=${mutableState.value.count}) emitted")
    }
}
