package org.karczelapp.withinrange.dataclass

data class GroupInvitation(
    val senderId:String = "",
    val inviteeId:String = "",
    val status: InvitationStatus = InvitationStatus.SENT
    )