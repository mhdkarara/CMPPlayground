package com.example.cmpplayground.feature.playground.counter

import com.example.cmpplayground.core.flowinspector.recorder.InMemoryFlowEventRecorder
import kotlin.test.Test
import kotlin.test.assertEquals

class CounterViewModelTest {
    @Test fun initialStateAndActionsPublishNewValues() {
        val vm = CounterViewModel(InMemoryFlowEventRecorder())
        assertEquals(CounterUiState(0), vm.state.value)
        vm.increment("one")
        assertEquals(CounterUiState(1), vm.state.value)
        vm.decrement("two")
        assertEquals(CounterUiState(0), vm.state.value)
        vm.increment("three")
        vm.reset("four")
        assertEquals(CounterUiState(0), vm.state.value)
    }
}
