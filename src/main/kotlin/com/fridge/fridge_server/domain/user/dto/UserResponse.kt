package com.fridge.fridge_server.domain.user.dto

import com.fridge.fridge_server.domain.user.User

data class UserLoginResponse(
    val id: Long,
    val name: String,
    val email: String,
    val familyGroupId: Long,
) {
    companion object {
        fun from(user: User): UserLoginResponse {
            return UserLoginResponse(user.id, user.name, user.email, user.familyGroup!!.id)
        }
    }
}

data class UserInfoResponse(
    val userId: Long,
    val userName: String,
    val userProfile: Int,
    val email: String,
    val familyGroupId: Long,
    val familyGroupName: String,
    val fridges: List<UserFridgeInfo>
)

data class UserFridgeInfo(
    val id: Long,
    val name: String
)

data class UserSummaryResponse(
    val id: Long,
    val name: String,
    val email: String,
    val profile: Int,
) {
    companion object {
        fun from(user: User): UserSummaryResponse {
            return UserSummaryResponse(user.id, user.name, user.email, user.profile)
        }
    }
}
