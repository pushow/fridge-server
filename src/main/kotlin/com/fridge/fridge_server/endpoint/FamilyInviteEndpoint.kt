package com.fridge.fridge_server.endpoint

import com.fridge.fridge_server.domain.auth.CurrentUser.CurrentUser
import com.fridge.fridge_server.domain.auth.UserPrincipal
import com.fridge.fridge_server.domain.familyinvite.FamilyInvite
import com.fridge.fridge_server.domain.familyinvite.FamilyInviteService
import com.fridge.fridge_server.domain.familyinvite.dto.FamilyInviteRequest
import com.fridge.fridge_server.domain.familyinvite.dto.FamilyInviteResponse
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/invites")
@SecurityRequirement(name = "bearerAuth")
class FamilyInviteEndpoint(
    private val familyInviteService: FamilyInviteService
) {

    // 초대 전송
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun sendInvite(
        @Parameter(hidden = true) @CurrentUser user: UserPrincipal,
        @RequestParam(required = true) email: String,
    ) : FamilyInvite {
        return familyInviteService.sendInvite(
            fromFamilyId = user.getFamilyId(),
            inviterName = user.getUser().name,
            inviterEmail = user.getUser().email,
            toUserEmail = email
        )
    }

    // 유저가 받은 초대 목록 조회
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getMyInvites(@Parameter(hidden = true) @CurrentUser user: UserPrincipal): List<FamilyInviteResponse> {
        val userId = user.getUser().id
        val invites = familyInviteService.getPendingInvitesForUser(userId)
        return invites.map { FamilyInviteResponse.from(it) }
    }

    // 초대 수락
    @PostMapping("/{invitationId}/accept")
    @ResponseStatus(HttpStatus.OK)
    fun acceptInvite(
        @PathVariable invitationId: Long,
        @Parameter(hidden = true) @CurrentUser user: UserPrincipal,
    ) {
        familyInviteService.acceptInvite(invitationId, user.getUser().id)
    }

    // 초대 거절
    @PostMapping("/{invitationId}/decline")
    @ResponseStatus(HttpStatus.OK)
    fun declineInvite(
        @PathVariable invitationId: Long,
        @Parameter(hidden = true) @CurrentUser user: UserPrincipal,
    ) {
        familyInviteService.declineInvite(invitationId, user.getUser().id)
    }
}
