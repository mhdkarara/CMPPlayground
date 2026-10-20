package com.example.cmpplayground.feature.playground.calculator

import androidx.lifecycle.ViewModel
import com.example.cmpplayground.core.flowinspector.model.FlowLayer
import com.example.cmpplayground.core.flowinspector.recorder.FlowEventRecorder
import com.example.cmpplayground.core.flowinspector.recorder.InMemoryFlowEventRecorder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// SwiftUI analogy: ObservableObject. The View sends actions and observes read-only state.
class CalculatorViewModel(
    val recorder: FlowEventRecorder = InMemoryFlowEventRecorder(),
) : ViewModel() {
    private val mutableState = MutableStateFlow(CalculatorState())
    val state = mutableState.asStateFlow()
    private var nextOperation = 0L

    fun onAction(action: CalculatorAction, operationId: String = "calculator-${nextOperation++}") {
        recorder.record(operationId, FlowLayer.VIEW_MODEL, "CalculatorViewModel.onAction(${action.label()}) invoked")
        mutableState.update { CalculatorModel.reduce(it, action) }
        recorder.record(operationId, FlowLayer.STATE_FLOW, "CalculatorState(display=${mutableState.value.display}) emitted")
    }
}

private fun CalculatorAction.label(): String = when (this) {
    is CalculatorAction.Digit -> "Digit($value)"
    is CalculatorAction.ChooseOperation -> "ChooseOperation(${value.symbol})"
    CalculatorAction.Decimal -> "Decimal"
    CalculatorAction.Equals -> "Equals"
    CalculatorAction.Clear -> "Clear"
    CalculatorAction.Backspace -> "Backspace"
    CalculatorAction.ToggleSign -> "ToggleSign"
    CalculatorAction.Percent -> "Percent"
    CalculatorAction.ClearHistory -> "ClearHistory"
    is CalculatorAction.Recall -> "Recall"
}
