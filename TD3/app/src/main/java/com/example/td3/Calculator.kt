package com.example.td3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.td3.ui.theme.actionColor
import com.example.td3.ui.theme.backgroundColor
import com.example.td3.ui.theme.displayColor
import com.example.td3.ui.theme.numberColor
import com.example.td3.ui.theme.operatorColor
import com.example.td3.ui.theme.textColor


@Composable
fun CalculatorScreen() {
    val columnsAmount = 4
    val items = listOf(
        "C", "÷", "×", "⌫",
        "7", "8", "9", "-",
        "4", "5", "6", "+",
        "1", "2", "3", "=",
        "0", ".","", ""
    )

    var displayValue = remember { mutableStateOf("0") }
    var firstValue = remember { mutableStateOf("") }
    var lastValue = remember { mutableStateOf("") }
    var storedOperator = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        // Display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f)
                .padding(bottom = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                if (storedOperator.value.isNotEmpty()) {
                    Text(
                        text = "${firstValue.value} ${storedOperator.value}",
                        color = textColor.copy(alpha = 0.7f),
                        fontSize = 24.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                Text(
                    text = displayValue.value,
                    color = textColor,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Keypad
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(displayColor)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            val chunkedItems = items.chunked(columnsAmount)

            chunkedItems.forEach { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowItems.forEach { label ->
                        CalculatorButton(
                            label = label,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                handleButtonClick(
                                    label = if (label == "×") "x" else label,
                                    dataCalcul = displayValue,
                                    storedOperator = storedOperator,
                                    FirstValue = firstValue,
                                    LastValue = lastValue
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(8.dp)
            .aspectRatio(1f),
        shape = CircleShape,
    ) {
        Text(
            text = label,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

fun ComputeCalcul(
    Operator: String,
    First: MutableState<String>,
    Last: MutableState<String>
): Float {
    val firstValue = First.value.toFloatOrNull() ?: 0f
    val lastValue = Last.value.toFloatOrNull() ?: 0f

    return when (Operator) {
        "+" -> firstValue + lastValue
        "-" -> firstValue - lastValue
        "÷" -> if (lastValue != 0f) firstValue / lastValue else Float.NaN
        "x" -> firstValue * lastValue
        else -> 0f
    }
}

fun handleButtonClick(
    label: String,
    dataCalcul: MutableState<String>,
    storedOperator: MutableState<String>,
    FirstValue: MutableState<String>,
    LastValue: MutableState<String>
) {
    when {
        label == "C" -> {
            dataCalcul.value = "0"
            storedOperator.value = ""
            FirstValue.value = ""
            LastValue.value = ""
        }

        label == "⌫" -> {
            if (LastValue.value.isNotEmpty()) {
                LastValue.value = LastValue.value.dropLast(1)
                dataCalcul.value = if (LastValue.value.isEmpty()) "0" else LastValue.value
            } else if (storedOperator.value.isNotEmpty()) {
                storedOperator.value = ""
            } else if (FirstValue.value.isNotEmpty()) {
                FirstValue.value = FirstValue.value.dropLast(1)
                dataCalcul.value = if (FirstValue.value.isEmpty()) "0" else FirstValue.value
            }
        }

        label == "=" -> {
            if (storedOperator.value.isNotEmpty() && LastValue.value.isNotEmpty()) {
                val result = ComputeCalcul(storedOperator.value, FirstValue, LastValue)
                val formattedResult = if (result == result.toInt().toFloat()) {
                    result.toInt().toString()
                } else {
                    result.toString()
                }

                dataCalcul.value = formattedResult
                FirstValue.value = formattedResult
                LastValue.value = ""
                storedOperator.value = ""
            }
        }

        label.matches(Regex("[0-9.]")) -> {
            if (label == "." && (
                        (storedOperator.value.isEmpty() && FirstValue.value.contains(".")) ||
                                (storedOperator.value.isNotEmpty() && LastValue.value.contains("."))
                        )) {
                return
            }

            if (storedOperator.value.isEmpty()) {
                if (FirstValue.value == "0" && label != ".") {
                    FirstValue.value = label
                } else {
                    FirstValue.value += label
                }
                dataCalcul.value = FirstValue.value
            } else {
                if (LastValue.value == "0" && label != ".") {
                    LastValue.value = label
                } else {
                    LastValue.value += label
                }
                dataCalcul.value = LastValue.value
            }
        }

        label in listOf("+", "-", "x", "÷") -> {
            if (FirstValue.value.isEmpty()) {
                FirstValue.value = "0"
            }

            if (LastValue.value.isNotEmpty() && storedOperator.value.isNotEmpty()) {
                // Calculate previous operation first
                val result = ComputeCalcul(storedOperator.value, FirstValue, LastValue)
                val formattedResult = if (result == result.toInt().toFloat()) {
                    result.toInt().toString()
                } else {
                    result.toString()
                }

                FirstValue.value = formattedResult
                LastValue.value = ""
                dataCalcul.value = formattedResult
            }

            storedOperator.value = label
        }
    }
}
