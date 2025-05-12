package org.karczelapp.withinrange

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.karczelapp.withinrange.dataclass.Schedule
import org.karczelapp.withinrange.ui.theme.WithinRangeTheme


@Composable
fun DashboardScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Dashboard Screen",
        modifier = modifier
    )
    val mockSchedule = remember {
        listOf(
            Schedule(uid = "user1", taskIds = listOf("Task A", "Task B")),
            Schedule(uid = "user2", taskIds = listOf("Task C", "Task D", "Task E"))
        )
    }

    Surface(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            items(mockSchedule) { schedule ->
                DashboardCard(schedule)
            }
        }
    }
}

@Composable
fun DashboardCard(schedule: Schedule) {
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
fun DashboardScreenPreview() {
    WithinRangeTheme {
        DashboardScreen()
    }
}