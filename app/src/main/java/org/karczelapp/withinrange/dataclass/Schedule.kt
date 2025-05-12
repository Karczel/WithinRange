package org.karczelapp.withinrange.dataclass

data class Schedule(
    val uid:String = "",
    val taskIds: List<String>? = listOf()
)