package com.example.classiccarchecklist.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Widget for YES/NO checklist items
 * Uses FilterChips following Material Design 3 guidelines
 */
@Composable
fun YesNoItemWidget(
    currentValue: String?,
    onValueChanged: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val isYesSelected = currentValue == "YES"
    val isNoSelected = currentValue == "NO"
    
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // YES option
        FilterChip(
            selected = isYesSelected,
            onClick = {
                if (isYesSelected) {
                    // Allow deselecting by clicking again
                    onValueChanged(null)
                } else {
                    onValueChanged("YES")
                }
            },
            label = { Text("Yes") },
            leadingIcon = if (isYesSelected) {
                {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else null
        )
        
        // NO option
        FilterChip(
            selected = isNoSelected,
            onClick = {
                if (isNoSelected) {
                    // Allow deselecting by clicking again
                    onValueChanged(null)
                } else {
                    onValueChanged("NO")
                }
            },
            label = { Text("No") },
            leadingIcon = if (isNoSelected) {
                {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else null
        )
    }
}
