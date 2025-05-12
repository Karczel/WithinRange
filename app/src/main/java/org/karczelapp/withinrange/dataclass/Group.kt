package org.karczelapp.withinrange.dataclass

data class Group(
    val groupId:String = "",
    val groupName:String? = "",
    val invitations:List<GroupInvitation>? = listOf()
)