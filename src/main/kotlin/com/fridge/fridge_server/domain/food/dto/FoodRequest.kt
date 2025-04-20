package com.fridge.fridge_server.domain.food.dto
import com.fridge.fridge_server.domain.food.StorageType
import java.time.LocalDate
import java.time.LocalDateTime

data class CreateFoodRequest(
    val name: String,
    val expiryDate: LocalDateTime,
    val count: Long,
    val memo: String? = null,
    val storageType: StorageType? = null,  // null이면 기본값 COLD
    val fridgeId: Long
)

data class UpdateFoodRequest(
    val name: String,
    val expiryDate: LocalDateTime,
    val count: Long,
    val memo: String?,
    val storageType: StorageType,
    val icon : Int,
)