package org.karczelapp.withinrange.dataclass

import androidx.compose.ui.graphics.Color

enum class InvitationStatus(val displayText: String, val color: Color) {
    REJECTED("The invitation was Rejected", Color.Red),
    ACCEPTED("The invitation was Accepted", Color.Blue),
    SENT("Sent!", Color.DarkGray)
}