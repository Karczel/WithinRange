package org.karczelapp.withinrange

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.karczelapp.withinrange.ui.theme.WithinRangeTheme


@Composable
fun DashboardScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Dashboard Screen",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    WithinRangeTheme {
        DashboardScreen()
    }
}