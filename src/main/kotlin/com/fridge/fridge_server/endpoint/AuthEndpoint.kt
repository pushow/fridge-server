package com.fridge.fridge_server.endpoint

import com.fridge.fridge_server.domain.auth.AuthService
import com.fridge.fridge_server.domain.auth.dto.LoginRequest
import com.fridge.fridge_server.domain.auth.dto.TokenReissueRequest
import com.fridge.fridge_server.domain.auth.dto.TokenResponse
import com.fridge.fridge_server.domain.user.UserService
import com.fridge.fridge_server.domain.user.dto.CreateUserRequest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthEndpoint(
    private val authService: AuthService,
    private val userService: UserService
) {
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody dto: CreateUserRequest){
        userService.createUser(dto)
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    fun login(@RequestBody request: LoginRequest): TokenResponse {
        return authService.login(request)
    }

    @PostMapping("/reissue")
    @ResponseStatus(HttpStatus.OK)
    fun reissue(@RequestBody request: TokenReissueRequest): TokenResponse {
        return authService.reissue(request)
    }
}