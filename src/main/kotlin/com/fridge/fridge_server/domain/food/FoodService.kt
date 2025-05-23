package com.fridge.fridge_server.domain.food
import com.fridge.fridge_server.domain.food.dto.CreateFoodRequest
import com.fridge.fridge_server.domain.food.dto.UpdateFoodRequest
import com.fridge.fridge_server.domain.fridge.FridgeRepository
import com.fridge.fridge_server.common.CustomException
import com.fridge.fridge_server.common.ErrorCode
import com.fridge.fridge_server.domain.family.FamilyGroupRepository

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface FoodUseCase{
    fun getFoodsByFridge(fridgeId: Long): List<Food>
    fun getFoodsByFamilyGroup(familyGroupId: Long): List<Food>
    fun createFood(request: CreateFoodRequest): Food
    fun updateFood(foodId: Long, request: UpdateFoodRequest): Food
    fun deleteFood(foodId: Long)
}

@Service
class FoodService(
    private val foodRepository: FoodRepository,
    private val fridgeRepository: FridgeRepository,
    private val familyGroupRepository: FamilyGroupRepository
) : FoodUseCase {

    @Transactional(readOnly = true)
    override fun getFoodsByFridge(fridgeId: Long): List<Food> {
        val fridge = findFridgeOrThrow(fridgeId)
        return foodRepository.findAllByFridgeOrderByExpiryDateAsc(fridge)
    }
    @Transactional(readOnly = true)
    override fun getFoodsByFamilyGroup(familyGroupId: Long): List<Food> {
        val family = familyGroupRepository.findById(familyGroupId)
            .orElseThrow { CustomException(ErrorCode.FAMILY_NOT_FOUND) }

        val fridges = fridgeRepository.findAllByFamilyGroup(family)
        return fridges.flatMap { foodRepository.findAllByFridgeOrderByExpiryDateAsc(it) }
    }

    @Transactional
    override fun createFood(request: CreateFoodRequest): Food {
        val fridge = findFridgeOrThrow(request.fridgeId)
        val food = Food(
            name = request.name,
            expiryDate = request.expiryDate,
            count = request.count,
            memo = request.memo,
            storageType = request.storageType ?: StorageType.COLD,
            icon = request.icon,
            fridge = fridge
        )
        return foodRepository.save(food)
    }

    @Transactional
    override fun updateFood(foodId: Long, request: UpdateFoodRequest): Food {
        val food = findFoodOrThrow(foodId)
        food.name = request.name
        food.expiryDate = request.expiryDate
        food.count = request.count
        food.memo = request.memo
        food.storageType = request.storageType
        food.icon = request.icon
        return food
    }

    @Transactional
    override fun deleteFood(foodId: Long) {
        if (!foodRepository.existsById(foodId)) {
            throw CustomException(ErrorCode.FOOD_NOT_FOUND)
        }
        foodRepository.deleteById(foodId)
    }

    private fun findFridgeOrThrow(fridgeId: Long) =
        fridgeRepository.findById(fridgeId)
            .orElseThrow { CustomException(ErrorCode.FRIDGE_NOT_FOUND) }

    private fun findFoodOrThrow(foodId: Long) =
        foodRepository.findById(foodId)
            .orElseThrow { CustomException(ErrorCode.FOOD_NOT_FOUND) }
}