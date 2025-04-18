package com.fridge.fridge_server.endpoint

import com.fridge.fridge_server.domain.auth.CurrentUser.CurrentUser
import com.fridge.fridge_server.domain.auth.UserPrincipal
import com.fridge.fridge_server.domain.family.FamilyGroupService
import com.fridge.fridge_server.domain.family.dto.UpdateFamilyGroupRequest
import com.fridge.fridge_server.domain.user.dto.UserSummaryResponse
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/family-groups")
@SecurityRequirement(name = "bearerAuth")
class FamilyGroupEndpoint(private val familyGroupService: FamilyGroupService) {
    @GetMapping("/members")
    @ResponseStatus(HttpStatus.OK)
    fun getFamilyMembers(
        @Parameter(hidden = true) @CurrentUser user: UserPrincipal,
    ): List<UserSummaryResponse> {
        return familyGroupService.getMembers(user.getFamilyId())
            .map { UserSummaryResponse.from(it) }
    }

    @PutMapping("/name")
    @ResponseStatus(HttpStatus.OK)
    fun updateFamilyName(
        @Parameter(hidden = true) @CurrentUser user: UserPrincipal,
        @RequestBody request: UpdateFamilyGroupRequest
    ): String {
        val updated = familyGroupService.updateFamilyGroupName(user.getFamilyId(), request.name)
        return updated.name
    }

    @PostMapping("/leave")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun leaveFamily(@Parameter(hidden = true) @CurrentUser user: UserPrincipal,) {
        familyGroupService.leaveFamilyGroup(user.getUser().id)
    }
}