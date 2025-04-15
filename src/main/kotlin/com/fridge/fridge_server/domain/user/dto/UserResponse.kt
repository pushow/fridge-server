package com.fridge.fridge_server.domain.user.dto

import com.fridge.fridge_server.domain.user.User

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String
) {
    companion object {
        fun from(user: User): UserResponse {
            return UserResponse(user.id, user.name, user.email)
        }
    }
}