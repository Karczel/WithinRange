package org.karczelapp.withinrange

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.karczelapp.withinrange.dataclass.InvitationStatus


@Composable
fun InvitationStatusAlertDialog(status: InvitationStatus, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onClose() },
        title = {
            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = status.displayText,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = status.color
                )
            }
        },
        confirmButton = {
            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Button(onClick = onClose) {
                    Text("Close")
                }
            }
        }
    )
}

@Composable
fun AcceptInvitationForm(
    groupId: String,
    userId: String,
    onAccepted: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Test",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        // TODO: Real logic here later
                        onAccepted()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Accept Invitation")
                }
            }
        }
    }
}

@Composable
fun SendInvitationForm(
    onSend: (inviteeId: String, groupId: String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        ) {
            var inviteeId by remember { mutableStateOf("") }
            var groupId by remember { mutableStateOf("") }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = inviteeId,
                    onValueChange = { inviteeId = it },
                    label = { Text("Invitee User ID") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = groupId,
                    onValueChange = { groupId = it },
                    label = { Text("Group ID") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
                Button(
                    onClick = {
                        // TODO: Create GroupInvitation object and add to Firestore
                        // db.collection("GroupInvitation").add({
                        //   inviter = currentUserId,
                        //   invitee = inviteeId,
                        //   groupId = groupId,
                        //   status = "Sent"
                        // })
                        onSend(inviteeId, groupId)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Send Invitation")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GroupInvitationComponentPreview() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Inform status
        InvitationStatusAlertDialog(status = InvitationStatus.SENT) {
            // Close dialog logic
        }

        // Accept invite
        AcceptInvitationForm(groupId = "group123", userId = "user456",
            onAccepted = {},
            onDismiss = {})

        // Send invite
        SendInvitationForm ({ inviteeId, groupId ->
            // After sent logic
        },onDismiss = {})
    }
}