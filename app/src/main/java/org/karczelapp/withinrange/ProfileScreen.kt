package org.karczelapp.withinrange

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.karczelapp.withinrange.ui.theme.WithinRangeTheme

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Profile Screen",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    WithinRangeTheme {
        ProfileScreen()
    }
}