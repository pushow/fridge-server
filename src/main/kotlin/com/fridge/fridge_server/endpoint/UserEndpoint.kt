package com.fridge.fridge_server.endpoint

import com.fridge.fridge_server.domain.fridge.Fridge
import com.fridge.fridge_server.domain.user.UserService
import com.fridge.fridge_server.domain.user.dto.CreateUserRequest
import com.fridge.fridge_server.domain.user.dto.UserLoginRequest
import com.fridge.fridge_server.domain.user.dto.UserResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    fun login(@RequestBody request: UserLoginRequest): UserResponse {
        val user = userService.login(request)
        return UserResponse.from(user)
    }
//
//    @GetMapping("/{userId}")
//    fun getUser(@PathVariable userId: Long): ResponseEntity<Any> {
//        TODO("사용자 정보 조회")
//    }
}