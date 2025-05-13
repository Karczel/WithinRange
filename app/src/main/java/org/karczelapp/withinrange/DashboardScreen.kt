package org.karczelapp.withinrange

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.firestore
import org.karczelapp.withinrange.dataclass.Task
import org.karczelapp.withinrange.dataclass.TaskStatus
import org.karczelapp.withinrange.ui.theme.WithinRangeTheme


@Composable
fun DashboardScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Dashboard Screen",
        modifier = modifier
    )

    var tasks by remember { mutableStateOf<List<Pair<String, Task>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var auth: FirebaseAuth = Firebase.auth
    val user = auth.currentUser
    val authUid = user?.uid

    LaunchedEffect(authUid) {
        Firebase.firestore.collection("schedules")
            .get()
            .addOnSuccessListener { result ->
                Log.d("ScheduleScreen", "Firestore fetch success, documents: ${result.documents.size}")

                result.documents.forEach { doc ->
                    val scheduleId = doc.id

                    Firebase.firestore.collection("schedules")
                        .document(scheduleId)
                        .collection("tasks")
                        .orderBy("timeDue", com.google.firebase.firestore.Query.Direction.DESCENDING) // order by timestamp desc
                        .get()
                        .addOnSuccessListener { taskResult ->
                            val fetchedTasks = taskResult.documents.mapNotNull { taskDoc ->
                                val title = taskDoc.getString("title") ?: ""
                                val details = taskDoc.getString("details")
                                val location = taskDoc.getGeoPoint("location") ?: GeoPoint(0.0, 0.0)
                                val timeDue = taskDoc.getTimestamp("timeDue")
                                val statusStr = taskDoc.getString("status")
                                val status = try {
                                    TaskStatus.valueOf(statusStr ?: TaskStatus.TO_GO.name)
                                } catch (e: Exception) {
                                    TaskStatus.TO_GO
                                }

                                Pair(scheduleId, Task(title, details, location, timeDue, status))
                            }

                            tasks = tasks + fetchedTasks.sortedBy { it.second.timeDue }
                        }
                }
                isLoading = false
            }
            .addOnFailureListener { e ->
                Log.e("ScheduleScreen", "Firestore fetch failed", e)
                isLoading = false
            }
    }


    Surface(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            items(tasks) { taskPair ->
                DashboardTaskCard(taskPair)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

    }
}


@Composable
fun DashboardTaskCard(taskPair: Pair<String, Task>) {
    val (scheduleId, task) = taskPair
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "From Schedule ID: $scheduleId", style = MaterialTheme.typography.labelMedium)
            Text(text = "Title: ${task.title}", style = MaterialTheme.typography.titleMedium)
            task.details?.let { Text(text = "Details: $it", style = MaterialTheme.typography.bodyMedium) }
            Text(
                text = "Location: (${task.location.latitude}, ${task.location.longitude})",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Time Due: ${task.timeDue?.toDate()?.toString() ?: "No due date"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(text = "Status: ${task.status}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    WithinRangeTheme {
        DashboardScreen()
    }
}