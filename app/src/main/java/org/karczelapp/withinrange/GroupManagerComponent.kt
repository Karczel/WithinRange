package org.karczelapp.withinrange

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun GroupComponent() {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Select Group") }
    var groups by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Use LaunchedEffect to fetch data only once
    LaunchedEffect(Unit) {
        try {
            val result = FirebaseFirestore.getInstance().collection("groups").get().await()
            groups = result.documents.mapNotNull { it.getString("groupName") }
            isLoading = false
        } catch (e: Exception) {
            errorMessage = e.localizedMessage
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .wrapContentWidth()
            .padding(top = 16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            when {
                isLoading -> CircularProgressIndicator()
                errorMessage != null -> Text("Error: $errorMessage")
                else -> {
                    Button(onClick = { expanded = true }) {
                        Text(selectedText)
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        groups.forEach { group ->
                            DropdownMenuItem(
                                text = { Text(group) },
                                onClick = {
                                    selectedText = group
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GroupComponentPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        // In preview, no Firebase calls will work, so you can replace with mocked data if needed
        Text("Preview not showing Firebase data")
    }
}
