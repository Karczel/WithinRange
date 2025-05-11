package org.karczelapp.withinrange

sealed class DestinationScreens(val route: String) {
    object Profile : DestinationScreens("profile")
    object Map : DestinationScreens("map")
    object PathHistory : DestinationScreens("pathHistory")
    object ToGo : DestinationScreens("schedule")
    object GroupToGo : DestinationScreens("dashboard")
    object Settings : DestinationScreens("settings")
    object Help : DestinationScreens("help")
}
