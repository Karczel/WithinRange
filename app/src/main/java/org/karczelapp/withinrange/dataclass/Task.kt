package org.karczelapp.withinrange.dataclass

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class Task(
    val title:String = "",
    val details:String? = "",
    val location: GeoPoint = GeoPoint(0.0, 0.0),
    val timeDue: Timestamp? = null,
    val status:TaskStatus = TaskStatus.TO_GO,
)