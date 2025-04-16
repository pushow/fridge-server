package com.fridge.fridge_server.endpoint

import com.fridge.fridge_server.domain.auth.CurrentUser.CurrentUser
import com.fridge.fridge_server.domain.auth.UserPrincipal
import com.fridge.fridge_server.domain.user.UserService
import com.fridge.fridge_server.domain.user.dto.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserEndpoint(
    private val userService: UserService
) {
    @GetMapping("/me")
    fun getMyInfo(@CurrentUser user: UserPrincipal): UserInfoResponse {
        val userId = user.getUser().id!!
        return userService.getUserInfo(userId)
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateUser(
        @PathVariable userId: Long,
        @RequestBody request: UpdateUserRequest
    ): UserLoginResponse {
        val updated = userService.updateUser(userId, request)
        return UserLoginResponse.from(updated)
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable userId: Long) {
        userService.deleteUser(userId)
    }
}