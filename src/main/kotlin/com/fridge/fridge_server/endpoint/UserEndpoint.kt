package com.fridge.fridge_server.endpoint

import com.fridge.fridge_server.domain.user.UserService
import com.fridge.fridge_server.domain.user.dto.CreateUserRequest
import com.fridge.fridge_server.domain.user.dto.UserInfoResponse
import com.fridge.fridge_server.domain.user.dto.UserLoginRequest
import com.fridge.fridge_server.domain.user.dto.UserLoginResponse
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
}