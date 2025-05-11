package org.karczelapp.withinrange

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.karczelapp.withinrange.ui.theme.WithinRangeTheme

@Composable
fun HelpScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Help Screen",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun HelpScreenPreview() {
    WithinRangeTheme {
        HelpScreen()
    }
}