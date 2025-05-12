package org.karczelapp.withinrange.dataclass

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class PathHistory(
    val uid:String = "",
    val location: GeoPoint = GeoPoint(0.0, 0.0),
    val timestamp: Timestamp = Timestamp.Companion.now()
)