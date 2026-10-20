package com.example.cmpplayground.feature.playground.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.cmpplayground.core.designsystem.tokens.AppSpacing
import com.example.cmpplayground.feature.playground.calculator.CalculatorRoute
import com.example.cmpplayground.feature.playground.counter.CounterRoute

@Composable
fun PlaygroundScreen(selected: String, onSelectedChange: (String) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth().padding(horizontal = AppSpacing.screen, vertical = AppSpacing.sm)) {
            listOf("Counter", "Calculator").forEachIndexed { index, item ->
                SegmentedButton(selected == item, onClick = { onSelectedChange(item) },
                    shape = SegmentedButtonDefaults.itemShape(index, 2)) { Text(item) }
            }
        }
        when (selected) {
            "Counter" -> CounterRoute()
            else -> CalculatorRoute()
        }
    }
}
