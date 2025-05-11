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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.karczelapp.withinrange.DestinationScreens


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
    navController: NavHostController,
    drawerState: DrawerState,
    navSelectedItem: Int,
    onNavItemSelected: (Int) -> Unit,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menu", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
                HorizontalDivider()
                NavItemInfo().getAllNavItems().forEachIndexed { index, itemInfo ->
                    NavigationDrawerItem(
                        selected = (index == navSelectedItem),
                        onClick = {
                            onNavItemSelected(index)
                            navController.navigate(itemInfo.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            scope.launch { drawerState.close() }
                        },
                        icon = { Icon(imageVector = itemInfo.icon, contentDescription = itemInfo.label) },
                        label = { Text(text = itemInfo.label) }
                    )
                }
            }
        },
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithSidebar() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var navSelectedItem by rememberSaveable { mutableStateOf(0) }

    SidebarNavigationDrawer(
        navController = navController,
        drawerState = drawerState,
        navSelectedItem = navSelectedItem,
        onNavItemSelected = { navSelectedItem = it }
    ) {
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
            NavHost(
                navController = navController,
                startDestination = DestinationScreens.Map.route,
                modifier = Modifier.padding(padding)
            ) {
                composable(DestinationScreens.Map.route) { MapScreen() }
                composable(DestinationScreens.Profile.route) { ProfileScreen() }
                composable(DestinationScreens.PathHistory.route) { PathHistoryScreen() }
                composable(DestinationScreens.ToGo.route) { ScheduleScreen() }
                composable(DestinationScreens.GroupToGo.route) { DashboardScreen() }
                composable(DestinationScreens.Settings.route) { SettingsScreen() }
                composable(DestinationScreens.Help.route) { HelpScreen() }
            }
        }
    }
}