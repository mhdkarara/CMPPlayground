package com.example.cmpplayground.feature.playground.calculator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cmpplayground.core.flowinspector.model.FlowEvent
import com.example.cmpplayground.core.flowinspector.model.FlowLayer
import com.example.cmpplayground.core.flowinspector.recorder.FlowEventRecorder
import com.example.cmpplayground.core.flowinspector.ui.FlowInspector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

// Only this route knows about the ViewModel; the screen below can be previewed with plain state.
@Composable
fun CalculatorRoute(viewModel: CalculatorViewModel = viewModel { CalculatorViewModel() }) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events by viewModel.recorder.events.collectAsStateWithLifecycle()
    var inspectorEnabled by rememberSaveable { mutableStateOf(true) }
    var inspectorExpanded by rememberSaveable { mutableStateOf(false) }
    var nextOperation by rememberSaveable { mutableLongStateOf(0L) }
    var observedOperation by rememberSaveable { mutableStateOf("calculator-initial") }
    SideEffect { viewModel.recorder.setEnabled(inspectorEnabled) }
    LaunchedEffect(state) {
        viewModel.recorder.record(observedOperation, FlowLayer.UI_OBSERVATION,
            "Calculator screen collected display=${state.display}")
    }
    val sendAction: (CalculatorAction) -> Unit = { action ->
        val operationId = "calculator-${nextOperation++}"
        observedOperation = operationId
        viewModel.recorder.record(operationId, FlowLayer.UI, "Key action ${action.inspectorLabel()} selected")
        viewModel.onAction(action, operationId)
    }
    CalculatorScreen(state, sendAction, viewModel.recorder, events, inspectorEnabled,
        onInspectorEnabledChange = { inspectorEnabled = it }, inspectorExpanded = inspectorExpanded,
        onInspectorExpandedChange = { inspectorExpanded = it })
}

@Composable
fun CalculatorScreen(
    state: CalculatorState,
    onAction: (CalculatorAction) -> Unit,
    recorder: FlowEventRecorder? = null,
    events: List<FlowEvent> = emptyList(),
    inspectorEnabled: Boolean = true,
    onInspectorEnabledChange: (Boolean) -> Unit = {},
    inspectorExpanded: Boolean = false,
    onInspectorExpandedChange: (Boolean) -> Unit = {},
) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Column(Modifier.widthIn(max = 480.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Calculator", style = MaterialTheme.typography.headlineSmall)
            Text("Enter a calculation, then inspect how each action updates screen state.",
                style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(state.expression.ifEmpty { " " }, Modifier.fillMaxWidth(), textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(state.error ?: state.display, Modifier.fillMaxWidth().padding(vertical = 16.dp),
                textAlign = TextAlign.End,
                fontSize = when {
                    state.error != null || state.display.length > 16 -> 20.sp
                    state.display.length > 12 -> 24.sp
                    else -> 32.sp
                },
                color = if (state.error != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton({ onAction(CalculatorAction.Backspace) }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Delete last digit")
                }
            }
            val rows = listOf(
                listOf("AC", "+/-", "%", "/"),
                listOf("7", "8", "9", "*"),
                listOf("4", "5", "6", "-"),
                listOf("1", "2", "3", "+"),
                listOf("0", ".", "="),
            )
            rows.forEach { row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row.forEach { key ->
                        val selected = state.operation?.symbol == key
                        val label = when (key) {
                            "AC" -> "All clear"; "+/-" -> "Toggle sign"; "/" -> "Divide"
                            "*" -> "Multiply"; "-" -> "Subtract"; "+" -> "Add"
                            "=" -> "Equals"; "." -> "Decimal point"; "%" -> "Percent"
                            else -> key
                        }
                        FilledTonalButton(onClick = { onAction(actionForKey(key)) },
                            modifier = Modifier.weight(if (key == "0") 2f else 1f).height(56.dp)
                                .semantics { contentDescription = label },
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = if (selected || key == "=") MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surfaceContainerHigh,
                                contentColor = if (selected || key == "=") MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSurface)) { Text(key, fontSize = 20.sp) }
                    }
                }
            }
            HorizontalDivider(Modifier.padding(top = 16.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text("History", style = MaterialTheme.typography.titleMedium)
                IconButton({ onAction(CalculatorAction.ClearHistory) }, enabled = state.history.isNotEmpty()) {
                    Icon(Icons.Default.Delete, "Clear history")
                }
            }
            state.history.forEach { calculation ->
                ListItem(headlineContent = { Text(calculation.result) },
                    supportingContent = { Text(calculation.expression) },
                    modifier = Modifier.clickable { onAction(CalculatorAction.Recall(calculation.result)) })
            }
        }
        if (recorder != null) {
            HorizontalDivider(Modifier.padding(top = 12.dp))
            FlowInspector(events, inspectorEnabled, recorder, onInspectorEnabledChange,
                inspectorExpanded, onInspectorExpandedChange)
        }
    }
}

private fun CalculatorAction.inspectorLabel(): String = when (this) {
    is CalculatorAction.Digit -> "Digit $value"
    is CalculatorAction.ChooseOperation -> "${value.symbol} operation"
    CalculatorAction.Decimal -> "Decimal"
    CalculatorAction.Equals -> "Equals"
    CalculatorAction.Clear -> "Clear"
    CalculatorAction.Backspace -> "Backspace"
    CalculatorAction.ToggleSign -> "Toggle sign"
    CalculatorAction.Percent -> "Percent"
    CalculatorAction.ClearHistory -> "Clear history"
    is CalculatorAction.Recall -> "Recall result"
}

private fun actionForKey(key: String): CalculatorAction = when (key) {
    "AC" -> CalculatorAction.Clear
    "+/-" -> CalculatorAction.ToggleSign
    "%" -> CalculatorAction.Percent
    "." -> CalculatorAction.Decimal
    "=" -> CalculatorAction.Equals
    else -> Operation.entries.firstOrNull { it.symbol == key }?.let { CalculatorAction.ChooseOperation(it) }
        ?: CalculatorAction.Digit(key.toInt())
}
