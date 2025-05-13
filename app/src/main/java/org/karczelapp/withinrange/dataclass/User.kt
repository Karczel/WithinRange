package org.karczelapp.withinrange.dataclass

import androidx.compose.ui.graphics.Color

data class User(
    val name: String? = "",
    val groupIds: List<String>? = listOf(),
    val color: Color? = null,
    val profilePicUrl: String? = null,
    val pathHistory: List<PathHistory>? = listOf(),
)