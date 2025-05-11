package org.karczelapp.withinrange

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import org.karczelapp.withinrange.ui.theme.WithinRangeTheme

data class Schedule(
    val uid:String = "",
    val taskIds: List<String>? = listOf()
)

@Composable
fun ScheduleScreen(modifier: Modifier = Modifier) {
    val db = Firebase.firestore
    Text(
        text = "To Go Screen",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun ScheduleScreenPreview() {
    WithinRangeTheme {
        ScheduleScreen()
    }
}