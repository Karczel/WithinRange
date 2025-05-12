package org.karczelapp.withinrange.dataclass

data class User(
    val uid: String = "",
    val name: String? = "",
    val groupIds: List<String>? = listOf()
)