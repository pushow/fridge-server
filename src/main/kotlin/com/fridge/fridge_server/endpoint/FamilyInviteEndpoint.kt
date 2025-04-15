package com.fridge.fridge_server.endpoint

import com.fridge.fridge_server.domain.familyinvite.FamilyInviteService
import com.fridge.fridge_server.domain.familyinvite.dto.FamilyInviteRequest
import com.fridge.fridge_server.domain.familyinvite.dto.FamilyInviteResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/invites")
class FamilyInviteEndpoint(
    private val familyInviteService: FamilyInviteService
) {

    // 초대 전송
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun sendInvite(@RequestBody request: FamilyInviteRequest) {
        familyInviteService.sendInvite(
            fromFamilyId = request.fromFamilyGroupId,
            inviterName = request.inviterName,
            toUserId = request.toUserId
        )
    }

    // 유저가 받은 초대 목록 조회
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getMyInvites(@RequestParam userId: Long): List<FamilyInviteResponse> {
        val invites = familyInviteService.getPendingInvitesForUser(userId)
        return invites.map { FamilyInviteResponse.from(it) }
    }

    // 초대 수락
    @PostMapping("/{invitationId}/accept")
    @ResponseStatus(HttpStatus.OK)
    fun acceptInvite(@PathVariable invitationId: Long, @RequestParam userId: Long) {
        familyInviteService.acceptInvite(invitationId, userId)
    }

    // 초대 거절
    @PostMapping("/{invitationId}/decline")
    @ResponseStatus(HttpStatus.OK)
    fun declineInvite(@PathVariable invitationId: Long, @RequestParam userId: Long) {
        familyInviteService.declineInvite(invitationId, userId)
    }
}
