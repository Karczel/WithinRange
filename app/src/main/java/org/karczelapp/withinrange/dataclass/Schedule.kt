package org.karczelapp.withinrange.dataclass

data class Schedule(
    val uid:String = "",
    val tasks: List<Task>? = listOf()
)