package org.karczelapp.withinrange

import com.google.firebase.Timestamp


enum class InvitationStatus {
    SENT,
    ACCEPTED,
    REJECTED
}

data class GroupInvitation(
    val senderId:String = "",
    val inviteeId:String = "",
    val status: InvitationStatus = InvitationStatus.SENT
    )

data class Group(
    val groupId:String = "",
    val groupName:String? = "",
    val invitations:List<GroupInvitation>? = listOf()
)