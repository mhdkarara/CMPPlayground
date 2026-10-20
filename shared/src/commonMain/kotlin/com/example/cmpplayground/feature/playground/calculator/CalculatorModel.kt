package com.example.cmpplayground.feature.playground.calculator

import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.round

enum class Operation(val symbol: String) { Add("+"), Subtract("-"), Multiply("*"), Divide("/") }

sealed interface CalculatorAction {
    data class Digit(val value: Int) : CalculatorAction
    data class ChooseOperation(val value: Operation) : CalculatorAction
    data object Decimal : CalculatorAction
    data object Equals : CalculatorAction
    data object Clear : CalculatorAction
    data object Backspace : CalculatorAction
    data object ToggleSign : CalculatorAction
    data object Percent : CalculatorAction
    data object ClearHistory : CalculatorAction
    data class Recall(val value: String) : CalculatorAction
}

data class Calculation(val expression: String, val result: String)

// Immutable screen state. No Compose, UIKit, or Android types enter the Model.
data class CalculatorState(
    val display: String = "0",
    val stored: Double? = null,
    val operation: Operation? = null,
    val replaceInput: Boolean = false,
    val repeatOperation: Operation? = null,
    val repeatOperand: Double? = null,
    val expression: String = "",
    val error: String? = null,
    val history: List<Calculation> = emptyList(),
)

// A four-function calculator uses immediate execution, not expression parsing/precedence.
object CalculatorModel {
    fun reduce(state: CalculatorState, action: CalculatorAction): CalculatorState {
        if (action == CalculatorAction.ClearHistory) return state.copy(history = emptyList())
        if (action == CalculatorAction.Clear) return CalculatorState(history = state.history)
        if (action is CalculatorAction.Recall) {
            if (action.value.toDoubleOrNull()?.isFinite() != true) return state
            return CalculatorState(display = action.value, replaceInput = true, history = state.history)
        }
        val current = if (state.error != null) {
            when (action) {
                is CalculatorAction.Digit, CalculatorAction.Decimal -> CalculatorState(history = state.history)
                else -> return state
            }
        } else state
        return when (action) {
            is CalculatorAction.Digit -> {
                if (action.value !in 0..9) return current
                val input = if (current.replaceInput || current.display == "0" || 'E' in current.display) "${action.value}"
                    else current.display + action.value
                if (input.count { it.isDigit() } > 12) current else current.copy(display = input,
                    replaceInput = false, expression = if (current.operation == null) "" else current.expression,
                    repeatOperation = null, repeatOperand = null)
            }
            CalculatorAction.Decimal -> {
                val input = if (current.replaceInput || 'E' in current.display) "0" else current.display
                current.copy(display = if ('.' in input) input else "$input.", replaceInput = false,
                    repeatOperation = null, repeatOperand = null,
                    expression = if (current.operation == null) "" else current.expression)
            }
            CalculatorAction.Backspace -> if (current.replaceInput) current else if ('E' in current.display) current.copy(display = "0") else current.copy(
                display = current.display.dropLast(1).takeUnless { it.isEmpty() || it == "-" } ?: "0")
            CalculatorAction.ToggleSign -> current.copy(
                display = format(-current.display.toDouble()), repeatOperation = null, repeatOperand = null)
            CalculatorAction.Percent -> current.copy(display = format(current.display.toDouble() / 100),
                repeatOperation = null, repeatOperand = null)
            is CalculatorAction.ChooseOperation -> {
                val result = if (current.operation != null && !current.replaceInput) evaluate(current) else current
                if (result.error != null) result else result.copy(stored = result.display.toDouble(),
                    operation = action.value, replaceInput = true,
                    repeatOperation = null, repeatOperand = null,
                    expression = "${result.display} ${action.value.symbol}")
            }
            CalculatorAction.Equals -> evaluate(current)
            else -> current
        }
    }

    private fun evaluate(state: CalculatorState): CalculatorState {
        val operation = state.operation ?: state.repeatOperation ?: return state
        val left = state.stored ?: state.display.toDouble()
        val right = if (state.operation != null) state.display.toDouble() else state.repeatOperand ?: return state
        if (operation == Operation.Divide && right == 0.0) return state.copy(error = "Cannot divide by zero")
        val result = when (operation) {
            Operation.Add -> left + right
            Operation.Subtract -> left - right
            Operation.Multiply -> left * right
            Operation.Divide -> left / right
        }
        if (!result.isFinite()) return state.copy(error = "Result is out of range")
        val display = format(result)
        val expression = "${format(left)} ${operation.symbol} ${format(right)}"
        return state.copy(display = display, stored = null, operation = null, replaceInput = true,
            repeatOperation = operation, repeatOperand = right, expression = "$expression =",
            history = (listOf(Calculation(expression, display)) + state.history).take(20))
    }

    // Limit binary floating-point noise to 12 significant digits for a learning calculator.
    private fun format(value: Double): String {
        if (value == 0.0) return "0"
        val scale = 10.0.pow((11 - floor(log10(abs(value)))).coerceIn(-300.0, 300.0))
        val rounded = if ((value * scale).isFinite()) round(value * scale) / scale else value
        val text = rounded.toString()
        if ('E' !in text) return text.removeSuffix(".0")
        val (mantissa, exponentText) = text.split('E')
        val exponent = exponentText.toInt()
        if (exponent !in -12..12) return text
        val sign = if (mantissa.startsWith('-')) "-" else ""
        val unsigned = mantissa.removePrefix("-")
        val digits = unsigned.replace(".", "")
        val position = unsigned.substringBefore('.').length + exponent
        val plain = when {
            position <= 0 -> "0." + "0".repeat(-position) + digits
            position >= digits.length -> digits + "0".repeat(position - digits.length)
            else -> digits.take(position) + "." + digits.drop(position)
        }
        return sign + if ('.' in plain) plain.trimEnd('0').trimEnd('.') else plain
    }
}
