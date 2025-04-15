package com.fridge.fridge_server.domain.food
import com.fridge.fridge_server.domain.food.dto.CreateFoodRequest
import com.fridge.fridge_server.domain.food.dto.UpdateFoodRequest
import com.fridge.fridge_server.domain.fridge.FridgeRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class FoodService(
    private val foodRepository: FoodRepository,
    private val fridgeRepository: FridgeRepository
) {
    @Transactional
    fun createFood(request: CreateFoodRequest): Food {
        val fridge = fridgeRepository.findById(request.fridgeId)
            .orElseThrow { IllegalArgumentException("냉장고 없음") }

        val food = Food(
            name = request.name,
            expiryDate = request.expiryDate,
            count = request.count,
            memo = request.memo,
            storageType = request.storageType ?: StorageType.COLD,
            fridge = fridge
        )

        return foodRepository.save(food)
    }

    fun getFoodsByFridge(fridgeId: Long): List<Food> {
        val fridge = fridgeRepository.findById(fridgeId)
            .orElseThrow { IllegalArgumentException("냉장고 없음") }

        return foodRepository.findAllByFridgeOrderByExpiryDateAsc(fridge)
    }

    @Transactional
    fun updateFood(foodId: Long, request: UpdateFoodRequest): Food {
        val food = foodRepository.findById(foodId)
            .orElseThrow { IllegalArgumentException("음식 없음") }

        food.name = request.name
        food.expiryDate = request.expiryDate
        food.count = request.count
        food.memo = request.memo
        food.storageType = request.storageType

        return food
    }

    @Transactional
    fun deleteFood(foodId: Long) {
        if (!foodRepository.existsById(foodId)) {
            throw IllegalArgumentException("해당 음식 없음")
        }
        foodRepository.deleteById(foodId)
    }
}