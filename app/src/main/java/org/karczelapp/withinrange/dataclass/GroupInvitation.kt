package org.karczelapp.withinrange.dataclass

data class GroupInvitation(
    val invitationId:String = "",
    val senderId:String = "",
    val inviteeId:String = "",
    val status: InvitationStatus = InvitationStatus.SENT
    )