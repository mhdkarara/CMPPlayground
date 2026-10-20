package com.example.cmpplayground.core.flowinspector

import com.example.cmpplayground.core.flowinspector.model.FlowLayer
import com.example.cmpplayground.core.flowinspector.recorder.InMemoryFlowEventRecorder

import kotlin.test.Test
import kotlin.test.assertEquals

class FlowEventRecorderTest {
    @Test fun preservesOrderAndOperationAssociation() {
        val recorder = InMemoryFlowEventRecorder(clock = { 42L })
        recorder.record("add-1", FlowLayer.UI, "Tapped")
        recorder.record("add-1", FlowLayer.VIEW_MODEL, "Handled")
        assertEquals(listOf(FlowLayer.UI, FlowLayer.VIEW_MODEL), recorder.events.value.map { it.layer })
        assertEquals(listOf("add-1", "add-1"), recorder.events.value.map { it.operationId })
        assertEquals(listOf(42L, 42L), recorder.events.value.map { it.timestampMillis })
    }

    @Test fun boundedHistoryClearAndDisable() {
        val recorder = InMemoryFlowEventRecorder(capacity = 2)
        repeat(3) { recorder.record("op-$it", FlowLayer.UI, "event-$it") }
        assertEquals(listOf("op-1", "op-2"), recorder.events.value.map { it.operationId })
        recorder.setEnabled(false)
        recorder.record("ignored", FlowLayer.UI, "not recorded")
        assertEquals(2, recorder.events.value.size)
        recorder.clear()
        assertEquals(emptyList(), recorder.events.value)
        recorder.setEnabled(true)
        recorder.record("next", FlowLayer.UI, "recorded")
        assertEquals("next", recorder.events.value.single().operationId)
    }
}
