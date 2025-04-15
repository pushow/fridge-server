package com.fridge.fridge_server.domain.food.dto
import com.fridge.fridge_server.domain.food.StorageType
import java.time.LocalDate

data class CreateFoodRequest(
    val name: String,
    val expiryDate: LocalDate,
    val count: Long,
    val memo: String? = null,
    val storageType: StorageType? = null,  // null이면 기본값 COLD
    val fridgeId: Long
)

data class UpdateFoodRequest(
    val name: String,
    val expiryDate: LocalDate,
    val count: Long,
    val memo: String?,
    val storageType: StorageType
)