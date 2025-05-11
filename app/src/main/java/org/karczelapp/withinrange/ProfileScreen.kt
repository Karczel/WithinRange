package org.karczelapp.withinrange

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.karczelapp.withinrange.ui.theme.WithinRangeTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class User(
    val uid: String = "",
    val name: String? = "",
    val groupIds: List<String>? = listOf()
)

@Composable
fun ProfileScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 64.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile image (placeholder)
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )

            // Name
            Text(
                text = "Your Name",
                style = MaterialTheme.typography.titleLarge
            )

            // Email
            Text(
                text = "you@example.com",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            // Button
            Button(onClick = { /* TODO: Action */ }) {
                Text("Edit Profile")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    WithinRangeTheme {
        ProfileScreen()
    }
}