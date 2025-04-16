package com.fridge.fridge_server.endpoint

import com.fridge.fridge_server.domain.family.FamilyGroupService
import com.fridge.fridge_server.domain.family.dto.UpdateFamilyGroupRequest
import com.fridge.fridge_server.domain.user.dto.UserSummaryResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/family-groups")
@SecurityRequirement(name = "bearerAuth")
class FamilyGroupEndpoint(private val familyGroupService: FamilyGroupService) {
    @GetMapping("/{familyId}/members")
    @ResponseStatus(HttpStatus.OK)
    fun getFamilyMembers(@PathVariable familyId: Long): List<UserSummaryResponse> {
        return familyGroupService.getMembers(familyId)
            .map { UserSummaryResponse.from(it) }
    }

    @PutMapping("/{familyId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateFamilyName(
        @PathVariable familyId: Long,
        @RequestBody request: UpdateFamilyGroupRequest
    ): String {
        val updated = familyGroupService.updateFamilyGroupName(familyId, request.name)
        return updated.name
    }

    @PostMapping("/leave")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun leaveFamily(@RequestParam userId: Long) {
        familyGroupService.leaveFamilyGroup(userId)
    }
}