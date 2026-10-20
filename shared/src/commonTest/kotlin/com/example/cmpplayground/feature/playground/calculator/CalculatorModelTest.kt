package com.example.cmpplayground.feature.playground.calculator

import kotlin.test.*
import com.example.cmpplayground.core.flowinspector.model.FlowLayer
import com.example.cmpplayground.core.flowinspector.recorder.InMemoryFlowEventRecorder

class CalculatorModelTest {
    private fun calculate(vararg keys: String): CalculatorState = keys.fold(CalculatorState()) { state, key ->
        CalculatorModel.reduce(state, when (key) {
            "=" -> CalculatorAction.Equals
            "." -> CalculatorAction.Decimal
            "+/-" -> CalculatorAction.ToggleSign
            "%" -> CalculatorAction.Percent
            "back" -> CalculatorAction.Backspace
            "AC" -> CalculatorAction.Clear
            else -> Operation.entries.firstOrNull { it.symbol == key }?.let { CalculatorAction.ChooseOperation(it) }
                ?: CalculatorAction.Digit(key.toInt())
        })
    }

    @Test fun fourOperations() {
        assertEquals("5", calculate("2", "+", "3", "=").display)
        assertEquals("-1", calculate("2", "-", "3", "=").display)
        assertEquals("6", calculate("2", "*", "3", "=").display)
        assertEquals("0.5", calculate("2", "/", "4", "=").display)
    }

    @Test fun immediateExecutionAndRepeatedEquals() {
        assertEquals("20", calculate("2", "+", "3", "*", "4", "=").display)
        assertEquals("8", calculate("2", "+", "3", "=", "=").display)
        assertEquals("6", calculate("2", "+", "*", "3", "=").display)
    }

    @Test fun decimalInputAndFloatingPointNoise() {
        assertEquals("0.3", calculate("0", ".", "1", "+", "0", ".", "2", "=").display)
        assertEquals("0.12", calculate(".", ".", "1", "2").display)
        assertEquals("0", calculate("1", "back").display)
        assertEquals("-12", calculate("1", "+/-", "2").display)
        assertEquals("10000000", calculate("1", "0", "0", "0", "0", "0", "0", "+/-", "+/-", "0").display)
    }

    @Test fun percentAndNewInput() {
        assertEquals("0.5", calculate("5", "0", "%").display)
        assertEquals("7", calculate("2", "+", "3", "=", "7").display)
        assertEquals("0.2", calculate("2", "+", "3", "=", ".", "2").display)
    }

    @Test fun divisionByZeroAndRecovery() {
        assertEquals("Cannot divide by zero", calculate("1", "/", "0", "=").error)
        val recovered = calculate("1", "/", "0", "=", "8")
        assertEquals("8", recovered.display)
        assertNull(recovered.error)
        assertNull(recovered.operation)
    }

    @Test fun historyRecallAndClear() {
        val result = calculate("2", "+", "3", "=")
        assertEquals(Calculation("2 + 3", "5"), result.history.single())
        val cleared = CalculatorModel.reduce(result, CalculatorAction.Clear)
        assertEquals("0", cleared.display)
        assertEquals(result.history, cleared.history)
        val recalled = CalculatorModel.reduce(cleared, CalculatorAction.Recall("5"))
        assertEquals("5", recalled.display)
        assertNull(recalled.operation)
        assertTrue(CalculatorModel.reduce(recalled, CalculatorAction.ClearHistory).history.isEmpty())
        assertEquals(result, CalculatorModel.reduce(result, CalculatorAction.Recall("NaN")))
    }

    @Test fun historyAndInputAreBounded() {
        var state = calculate("1", "+", "1", "=")
        repeat(30) { state = CalculatorModel.reduce(state, CalculatorAction.Equals) }
        assertEquals(20, state.history.size)
        state = CalculatorState()
        repeat(30) { state = CalculatorModel.reduce(state, CalculatorAction.Digit(9)) }
        assertEquals(12, state.display.length)
        assertEquals(state, CalculatorModel.reduce(state, CalculatorAction.Digit(99)))
    }

    @Test fun viewModelPublishesModelResults() {
        val vm = CalculatorViewModel()
        vm.onAction(CalculatorAction.Digit(4))
        vm.onAction(CalculatorAction.ChooseOperation(Operation.Multiply))
        vm.onAction(CalculatorAction.Digit(5))
        vm.onAction(CalculatorAction.Equals)
        assertEquals("20", vm.state.value.display)
        assertEquals(1, vm.state.value.history.size)
    }

    @Test fun viewModelRecordsCalculatorOperationBoundaries() {
        val recorder = InMemoryFlowEventRecorder()
        val vm = CalculatorViewModel(recorder)
        vm.onAction(CalculatorAction.Digit(7), "calculator-7")
        val events = recorder.events.value
        assertEquals(listOf(FlowLayer.VIEW_MODEL, FlowLayer.STATE_FLOW), events.map { it.layer })
        assertEquals(listOf("calculator-7", "calculator-7"), events.map { it.operationId })
        assertTrue(events.last().description.contains("display=7"))
    }
}
