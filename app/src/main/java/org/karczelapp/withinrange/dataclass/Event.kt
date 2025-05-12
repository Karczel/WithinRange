package org.karczelapp.withinrange.dataclass

import com.google.firebase.Timestamp

data class Event(
    val uid:String = "",
    val taskId:String = "",
    val title:String = "",
    val details:String? = "",
    val timeDue: Timestamp? = null,
    val status:EventStatus = EventStatus.TO_GO,
)