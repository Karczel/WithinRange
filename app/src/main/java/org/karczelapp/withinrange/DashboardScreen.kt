package org.karczelapp.withinrange

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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
import org.karczelapp.withinrange.dataclass.Schedule
import org.karczelapp.withinrange.dataclass.Task
import org.karczelapp.withinrange.dataclass.TaskStatus
import org.karczelapp.withinrange.ui.theme.WithinRangeTheme


@Composable
fun DashboardScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Dashboard Screen",
        modifier = modifier
    )

    var schedules by remember { mutableStateOf<List<Schedule>>(emptyList()) }
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
                    val uid = doc.getString("uid") ?: return@forEach

                    // Fetch tasks subcollection of each schedule (if you moved to subcollection model)
                    Firebase.firestore.collection("schedules")
                        .document(scheduleId)
                        .collection("tasks")
                        .get()
                        .addOnSuccessListener { taskResult ->
                            val tasks = taskResult.documents.mapNotNull { taskDoc ->
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

                                Task(
                                    title = title,
                                    details = details,
                                    location = location,
                                    timeDue = timeDue,
                                    status = status
                                )
                            }

                            schedules = schedules + Schedule(uid = uid, tasks = tasks)
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
            items(schedules) { schedule ->
                DashboardCard(schedule)
            }
        }
    }
}

@Composable
fun DashboardCard(schedule: Schedule) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        schedule.tasks?.forEach { task ->
            TaskCard(task)
            Spacer(modifier = Modifier.height(8.dp))
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