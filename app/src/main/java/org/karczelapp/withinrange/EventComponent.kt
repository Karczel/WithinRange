package org.karczelapp.withinrange

import com.google.firebase.Timestamp

enum class EventStatus {
    ARRIVED,
    LATE,
    TO_GO
}

data class Event(
    val uid:String = "",
    val taskId:String = "",
    val title:String = "",
    val details:String? = "",
    val timeDue:Timestamp? = null,
    val status:EventStatus = EventStatus.TO_GO,
)