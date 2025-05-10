package org.karczelapp.withinrange

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.karczelapp.withinrange.ui.theme.WithinRangeTheme

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.graphics.vector.ImageVector


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WithinRangeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreenWithSidebar()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WithinRangeTheme {
        Greeting("Android")
    }
}

@Composable
fun SidebarNavigationDrawer(
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    onItemClick: (String) -> Unit = {},
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menu", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
                HorizontalDivider()

                SidebarItem("Profile", Icons.Default.Person, onClick = {
                    onItemClick("profile")
                    scope.launch { drawerState.close() }
                })
                SidebarItem("Map", Icons.Default.LocationOn, onClick = {
                    onItemClick("map")
                    scope.launch { drawerState.close() }
                })
                SidebarItem("Path History", Icons.Outlined.LocationOn, onClick = {
                    onItemClick("path_history")
                    scope.launch { drawerState.close() }
                })
                SidebarItem("To Go", Icons.Filled.DateRange, onClick = {
                    onItemClick("schedule")
                    scope.launch { drawerState.close() }
                })
                SidebarItem("Group To Go", Icons.Outlined.DateRange, onClick = {
                    onItemClick("dashboard")
                    scope.launch { drawerState.close() }
                })
                SidebarItem("Settings", Icons.Default.Settings, onClick = {
                    onItemClick("settings")
                    scope.launch { drawerState.close() }
                })
                SidebarItem("Help", Icons.Default.MoreVert, onClick = {
                    onItemClick("help")
                    scope.launch { drawerState.close() }
                })
            }
        },
        content = content
    )
}

@Composable
private fun SidebarItem(title: String, icon: ImageVector, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(title) },
        icon = { Icon(icon, contentDescription = title) },
        selected = false,
        onClick = onClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithSidebar() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    SidebarNavigationDrawer(drawerState = drawerState, onItemClick = {
        // Handle navigation based on key
    }) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("App") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                Text("Main content here")
            }
        }
    }
}