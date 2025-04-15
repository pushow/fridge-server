package com.fridge.fridge_server.domain.food.dto

import com.fridge.fridge_server.domain.food.Food
import com.fridge.fridge_server.domain.food.StorageType
import java.time.LocalDate

data class FoodResponse(
    val id: Long,
    val name: String,
    val expiryDate: LocalDate,
    val count: Long,
    val memo: String?,
    val storageType: StorageType,
    val fridgeId: Long
) {
    companion object {
        fun from(food: Food): FoodResponse {
            return FoodResponse(
                id = food.id,
                name = food.name,
                expiryDate = food.expiryDate,
                count = food.count,
                memo = food.memo,
                storageType = food.storageType,
                fridgeId = food.fridge.id
            )
        }
    }
}