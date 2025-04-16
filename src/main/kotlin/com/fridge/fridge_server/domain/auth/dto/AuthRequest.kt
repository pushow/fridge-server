package com.fridge.fridge_server.domain.auth.dto

data class LoginRequest(
    val email: String,
    val password: String
)

data class TokenReissueRequest(
    val refreshToken: String
)