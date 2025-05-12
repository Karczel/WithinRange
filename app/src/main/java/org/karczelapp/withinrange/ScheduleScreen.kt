package org.karczelapp.withinrange

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import org.karczelapp.withinrange.dataclass.Schedule
import org.karczelapp.withinrange.ui.theme.WithinRangeTheme

@Composable
fun ScheduleScreen(modifier: Modifier = Modifier) {
    val db = Firebase.firestore
    Text(
        text = "To Go Screen",
        modifier = modifier
    )
    val mockSchedule = remember {
        listOf(
            Schedule(uid = "user1", taskIds = listOf("Task A", "Task B")),
            Schedule(uid = "user1", taskIds = listOf("Task C", "Task D", "Task E"))
        )
    }

    Surface(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            items(mockSchedule) { schedule ->
                ScheduleCard(schedule)
            }
        }
    }
}

@Composable
fun ScheduleCard(schedule: Schedule) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "User ID: ${schedule.uid}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            schedule.taskIds?.forEach { taskId ->
                Text(text = "• $taskId", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleScreenPreview() {
    WithinRangeTheme {
        ScheduleScreen()
    }
}