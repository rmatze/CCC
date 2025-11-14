package com.example.classiccarchecklist.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/**
 * Widget for TEXT_INPUT checklist items
 * Uses OutlinedTextField following Material Design 3 guidelines
 */
@Composable
fun TextInputItemWidget(
    currentValue: String?,
    onValueChanged: (String?) -> Unit,
    placeholder: String = "Enter text",
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else 3
) {
    var textValue by remember { mutableStateOf(currentValue ?: "") }
    
    // Update local state when currentValue changes externally
    LaunchedEffect(currentValue) {
        textValue = currentValue ?: ""
    }
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = textValue,
            onValueChange = { newValue ->
                // Limit text length to prevent database issues
                val limitedValue = if (newValue.length > 500) newValue.take(500) else newValue
                textValue = limitedValue
                // Save non-empty values, or null if empty
                onValueChanged(if (limitedValue.isNotBlank()) limitedValue else null)
            },
            placeholder = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )
    }
}

