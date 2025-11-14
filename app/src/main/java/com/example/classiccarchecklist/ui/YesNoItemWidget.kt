package com.example.classiccarchecklist.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Widget for YES/NO checklist items
 * Uses radio buttons following Material Design 3 guidelines
 */
@Composable
fun YesNoItemWidget(
    currentValue: String?,
    onValueChanged: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val isYesSelected = currentValue == "YES"
    val isNoSelected = currentValue == "NO"
    val isAnswered = currentValue != null
    
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // YES option
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RadioButton(
                selected = isYesSelected,
                onClick = {
                    if (isYesSelected) {
                        // Allow deselecting by clicking again
                        onValueChanged(null)
                    } else {
                        onValueChanged("YES")
                    }
                }
            )
            Text(
                text = "YES",
                style = MaterialTheme.typography.bodyLarge,
                color = if (isYesSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
        
        // NO option
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RadioButton(
                selected = isNoSelected,
                onClick = {
                    if (isNoSelected) {
                        // Allow deselecting by clicking again
                        onValueChanged(null)
                    } else {
                        onValueChanged("NO")
                    }
                }
            )
            Text(
                text = "NO",
                style = MaterialTheme.typography.bodyLarge,
                color = if (isNoSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
    
    // Visual indicator for completed items
    if (isAnswered) {
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "✓ Answered",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

