package org.karczelapp.withinrange

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.ui.graphics.vector.ImageVector

class NavItemInfo(
    val label:String = "",
    val icon:ImageVector = Icons.Filled.Star,
    val route:String = ""
) {
    fun getAllNavItems() : List<NavItemInfo> {
        return listOf(
            NavItemInfo("Profile", Icons.Default.Person, DestinationScreens.Profile.route),
            NavItemInfo("Map", Icons.Default.LocationOn, DestinationScreens.Map.route),
            NavItemInfo("Path History", Icons.Outlined.LocationOn, DestinationScreens.PathHistory.route),
            NavItemInfo("To Go", Icons.Filled.DateRange, DestinationScreens.ToGo.route),
            NavItemInfo("Group To Go", Icons.Outlined.DateRange, DestinationScreens.GroupToGo.route),
            NavItemInfo("Settings", Icons.Default.Settings, DestinationScreens.Settings.route),
            NavItemInfo("Help", Icons.Default.MoreVert, DestinationScreens.Help.route),
            )
    }
}