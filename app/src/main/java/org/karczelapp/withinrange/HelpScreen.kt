package org.karczelapp.withinrange

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.karczelapp.withinrange.ui.theme.WithinRangeTheme

@Composable
fun HelpScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Help & FAQ", style = MaterialTheme.typography.headlineSmall)

        FAQItem(
            question = "How do I log in?",
            answer = "Enter your email and password on the login screen."
        )

        FAQItem(
            question = "How do I sign up?",
            answer = "Go to the sign-up screen and fill out your email and password."
        )

        FAQItem(
            question = "Why do you need my location?",
            answer = "We use location to help show relevant data near you."
        )

        FAQItem(
            question = "How do I log out?",
            answer = "Go to Settings and tap the Log Out button."
        )
    }
}

@Composable
fun FAQItem(question: String, answer: String) {
    Column {
        Text(text = question, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = answer, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun HelpScreenPreview() {
    WithinRangeTheme {
        HelpScreen()
    }
}
