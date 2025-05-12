package org.karczelapp.withinrange

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun GroupComponent() {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Select Group") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Button(onClick = { expanded = true }) {
            Text(selectedText)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Group 1") },
                onClick = {
                    selectedText = "Group 1"
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Group 2") },
                onClick = {
                    selectedText = "Group 2"
                    expanded = false
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GroupComponentPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        GroupComponent()
    }
}
