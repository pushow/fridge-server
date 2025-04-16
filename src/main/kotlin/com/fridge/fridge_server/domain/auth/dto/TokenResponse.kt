package com.fridge.fridge_server.domain.auth.dto

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)