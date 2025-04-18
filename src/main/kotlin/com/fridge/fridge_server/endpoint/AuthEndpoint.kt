package com.fridge.fridge_server.endpoint

import com.fridge.fridge_server.domain.auth.AuthService
import com.fridge.fridge_server.domain.auth.dto.LoginRequest
import com.fridge.fridge_server.domain.auth.dto.TokenReissueRequest
import com.fridge.fridge_server.domain.auth.dto.TokenResponse
import com.fridge.fridge_server.domain.user.UserService
import com.fridge.fridge_server.domain.user.dto.CreateUserRequest
import com.fridge.fridge_server.domain.user.dto.UserSummaryResponse
import io.swagger.v3.oas.annotations.Parameter
import com.fridge.fridge_server.domain.auth.CurrentUser.CurrentUser
import com.fridge.fridge_server.domain.auth.UserPrincipal

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
    fun createUser(@RequestBody dto: CreateUserRequest): UserSummaryResponse {
        return userService.createUser(dto)
            .let { UserSummaryResponse.from(it) }
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

    @GetMapping("/check-email")
    @ResponseStatus(HttpStatus.OK)
    fun checkEmail(@RequestParam email: String): Boolean {
        val available = userService.isEmailAvailable(email)
        return available
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(@Parameter(hidden = true) @CurrentUser user: UserPrincipal) {
        authService.logout(user.getUser().id)
    }
}