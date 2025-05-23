package com.fridge.fridge_server.endpoint

import com.fridge.fridge_server.domain.auth.CurrentUser.CurrentUser
import com.fridge.fridge_server.domain.auth.UserPrincipal
import com.fridge.fridge_server.domain.user.UserService
import com.fridge.fridge_server.domain.user.dto.*
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearerAuth")
class UserEndpoint(
    private val userService: UserService
) {
    @GetMapping("/me")
    fun getMyInfo(@Parameter(hidden = true) @CurrentUser user: UserPrincipal): UserInfoResponse {
        val userId = user.getUser().id
        return userService.getUserInfo(userId)
    }

    @GetMapping("/name")
    fun getAnotherUserName(
        @RequestParam userEmail: String,
    ): String{
        return userService.getUserNameByEmail(userEmail);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    fun updateUser(
        @Parameter(hidden = true) @CurrentUser user: UserPrincipal,
        @RequestBody request: UpdateUserRequest
    ): UserLoginResponse {
        val userId = user.getUser().id
        val updated = userService.updateUser(userId, request)
        return UserLoginResponse.from(updated)
    }

    @PutMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    fun updateUserProfile(
        @Parameter(hidden = true) @CurrentUser user: UserPrincipal,
        @RequestBody request: UpdateUserProfileRequest
    ): UserLoginResponse {
        val userId = user.getUser().id
        val updated = userService.updateUserProfile(userId, request)
        return UserLoginResponse.from(updated)
    }


    @PostMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    fun changePassword(
        @Parameter(hidden = true) @CurrentUser user: UserPrincipal,
        @RequestBody request: ChangePasswordRequest
    ) {
        userService.changePassword(user.getUser().id, request)
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@Parameter(hidden = true) @CurrentUser user: UserPrincipal,) {
        val userId = user.getUser().id
        userService.deleteUser(userId)
    }


}