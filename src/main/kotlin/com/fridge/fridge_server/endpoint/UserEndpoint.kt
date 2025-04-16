package com.fridge.fridge_server.endpoint

import com.fridge.fridge_server.domain.user.UserService
import com.fridge.fridge_server.domain.user.dto.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserEndpoint(
    private val userService: UserService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody dto: CreateUserRequest){
        userService.createUser(dto)
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    fun login(@RequestBody request: UserLoginRequest): UserLoginResponse {
        val user = userService.login(request)
        return UserLoginResponse.from(user)
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    fun getMyInfo(@RequestParam userId: Long): UserInfoResponse {
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