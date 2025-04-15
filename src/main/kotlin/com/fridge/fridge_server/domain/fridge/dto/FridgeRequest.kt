package com.fridge.fridge_server.domain.fridge.dto

data class CreateFridgeRequest(
    val familyGroupId: Long,
    val name: String
)

data class UpdateFridgeRequest(
    val name: String
)