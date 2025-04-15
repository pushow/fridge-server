package com.fridge.fridge_server.domain.familyinvite.dto

data class FamilyInviteRequest(
    val fromFamilyGroupId: Long,
    val inviterName: String,
    val toUserId: Long
)