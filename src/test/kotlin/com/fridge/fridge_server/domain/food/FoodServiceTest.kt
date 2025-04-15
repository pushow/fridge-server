package com.fridge.fridge_server.domain.food

import com.fridge.fridge_server.domain.fridge.FridgeService
import com.fridge.fridge_server.domain.fridge.dto.CreateFridgeRequest
import com.fridge.fridge_server.domain.user.UserService
import com.fridge.fridge_server.domain.user.dto.CreateUserRequest
import com.fridge.fridge_server.domain.food.dto.CreateFoodRequest
import com.fridge.fridge_server.domain.food.dto.UpdateFoodRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@Transactional
class FoodServiceTest @Autowired constructor(
    val foodService: FoodService,
    val fridgeService: FridgeService,
    val userService: UserService
) {

    fun setup(): Long {
        val user = userService.createUser(CreateUserRequest("푸드유저", "food@test.com", "1234"))
        val fridge = fridgeService.createFridge(CreateFridgeRequest(user.familyGroup.id, "테스트 냉장고"))
        return fridge.id
    }

    @Test
    fun `음식을 등록하면 냉장고에 저장된다`() {
        val fridgeId = setup()

        val food = foodService.createFood(
            CreateFoodRequest(
                name = "계란",
                expiryDate = LocalDate.now().plusDays(3),
                count = 10,
                storageType = StorageType.COLD,
                fridgeId = fridgeId
            )
        )

        assertEquals("계란", food.name)
        assertEquals(StorageType.COLD, food.storageType)
        assertEquals(fridgeId, food.fridge.id)
    }

    @Test
    fun `냉장고에서 음식 리스트를 유통기한 오름차순으로 조회할 수 있다`() {
        val fridgeId = setup()

        foodService.createFood(CreateFoodRequest("A", LocalDate.now().plusDays(5), 1, null, StorageType.COLD, fridgeId))
        foodService.createFood(CreateFoodRequest("B", LocalDate.now().plusDays(2), 1, null, StorageType.COLD, fridgeId))
        foodService.createFood(CreateFoodRequest("C", LocalDate.now().plusDays(10), 1, null, StorageType.COLD, fridgeId))

        val foods = foodService.getFoodsByFridge(fridgeId)

        assertEquals("B", foods[0].name)
        assertEquals("A", foods[1].name)
        assertEquals("C", foods[2].name)
    }

    @Test
    fun `음식을 수정하면 정보가 반영된다`() {
        val fridgeId = setup()
        val food = foodService.createFood(
            CreateFoodRequest("우유", LocalDate.now().plusDays(7), 2, "저지방", StorageType.COLD, fridgeId)
        )

        val updated = foodService.updateFood(
            foodId = food.id,
            request = UpdateFoodRequest(
                name = "저지방우유",
                expiryDate = LocalDate.now().plusDays(5),
                count = 3,
                memo = "더 저지방",
                storageType = StorageType.COLD
            )
        )

        assertEquals("저지방우유", updated.name)
        assertEquals(3, updated.count)
        assertEquals("더 저지방", updated.memo)
    }

    @Test
    fun `음식을 삭제하면 더 이상 조회되지 않는다`() {
        val fridgeId = setup()
        val food = foodService.createFood(
            CreateFoodRequest("삭제음식", LocalDate.now().plusDays(3), 1, null, StorageType.COLD, fridgeId)
        )

        foodService.deleteFood(food.id)

        val foods = foodService.getFoodsByFridge(fridgeId)
        assertTrue(foods.none { it.id == food.id })
    }
}