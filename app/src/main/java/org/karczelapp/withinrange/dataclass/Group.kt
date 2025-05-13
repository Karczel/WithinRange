package org.karczelapp.withinrange.dataclass

data class Group(
    val groupName:String? = "",
    val invitations:List<GroupInvitation>? = listOf()
)