package com.fridge.fridge_server.domain.familyinvite.dto

import com.fridge.fridge_server.domain.familyinvite.FamilyInvite
import java.time.LocalDateTime

data class FamilyInviteResponse(
    val invitationId: Long,
    val fromFamilyGroupId: Long,
    val fromFamilyGroupName: String,
    val inviterName: String,
    val inviterEmail: String,
    val invitedAt: LocalDateTime
) {
    companion object {
        fun from(invite: FamilyInvite): FamilyInviteResponse {
            return FamilyInviteResponse(
                invitationId = invite.id,
                fromFamilyGroupId = invite.fromFamily.id,
                fromFamilyGroupName = invite.fromFamily.name,
                inviterName = invite.inviterName,
                inviterEmail = invite.inviterEmail,
                invitedAt = invite.invitedAt
            )
        }
    }
}