package com.example.cmpplayground.feature.playground.counter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cmpplayground.core.flowinspector.model.FlowLayer
import com.example.cmpplayground.core.flowinspector.recorder.FlowEventRecorder
import com.example.cmpplayground.core.flowinspector.recorder.InMemoryFlowEventRecorder
import com.example.cmpplayground.core.flowinspector.ui.FlowInspector

@Composable
fun CounterRoute() {
    val vm: CounterViewModel = viewModel { CounterViewModel(InMemoryFlowEventRecorder()) }
    val state by vm.state.collectAsStateWithLifecycle()
    CounterScreen(state, vm.recorder, vm::increment, vm::decrement, vm::reset)
}

@Composable
private fun CounterScreen(
    state: CounterUiState,
    recorder: FlowEventRecorder,
    increment: (String) -> Unit,
    decrement: (String) -> Unit,
    reset: (String) -> Unit,
) {
    var recordingEnabled by rememberSaveable { mutableStateOf(true) }
    var inspectorExpanded by rememberSaveable { mutableStateOf(false) }
    var nextOperation by rememberSaveable { mutableLongStateOf(0L) }
    var lastOperationId by rememberSaveable { mutableStateOf("counter-initial") }
    val events by recorder.events.collectAsStateWithLifecycle()
    LaunchedEffect(state.count, recordingEnabled) {
        recorder.setEnabled(recordingEnabled)
        if (recordingEnabled) recorder.record(lastOperationId, FlowLayer.UI_OBSERVATION, "Counter screen collected count=${state.count}")
    }
    fun sendAction(label: String, action: (String) -> Unit) {
        val operationId = "counter-${nextOperation++}"
        lastOperationId = operationId
        recorder.record(operationId, FlowLayer.UI, "$label tapped")
        action(operationId)
    }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Counter", style = MaterialTheme.typography.headlineSmall)
        Text("A small stateful app backed by ViewModel and StateFlow.", style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("${state.count}", style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(onClick = { sendAction("Decrement", decrement) }) { Text("−") }
            Button(onClick = { sendAction("Increment", increment) }, modifier = Modifier.weight(1f)) { Text("Increment") }
            TextButton(onClick = { sendAction("Reset", reset) }) { Text("Reset") }
        }
        HorizontalDivider()
        FlowInspector(events, recordingEnabled, recorder, { recordingEnabled = it },
            inspectorExpanded, { inspectorExpanded = it })
    }
}
