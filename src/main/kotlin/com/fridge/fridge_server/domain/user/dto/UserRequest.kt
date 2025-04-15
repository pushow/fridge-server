package com.fridge.fridge_server.domain.user.dto

data class CreateUserRequest(
    val name: String,
    val email: String,
    val password: String
)

data class UserLoginRequest(
    val email: String,
    val password: String
)