package org.karczelapp.withinrange

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun EventComponent(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Event name",
                style = MaterialTheme.typography.headlineLarge,
                )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Details",
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Time :",
                style = MaterialTheme.typography.bodyMedium,
            )
            //mapDisplay with location as center; show map preview fit in a box
            //how to select location?
        // 1. click -> map view -> choose pin -> assign location to task, handled in map view
        // 2. todo search location (extra)
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                contentAlignment = Alignment.CenterEnd
            ){
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.wrapContentWidth()
                ){
                    Button(
                        onClick = {
                            // TODO: Real logic here later
                        },
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {
                        if (true) {
                            Text("Confirm")
                        } else {
                            Text("Add")
                        }
                    }
                    Button(
                        onClick = {
                            // TODO: Real logic here later
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventComponentPreview() {
    EventComponent(onDismiss = {})
}