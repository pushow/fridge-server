package com.fridge.fridge_server.endpoint

import com.fridge.fridge_server.domain.food.Food
import com.fridge.fridge_server.domain.food.FoodService
import com.fridge.fridge_server.domain.food.dto.CreateFoodRequest
import com.fridge.fridge_server.domain.food.dto.FoodResponse
import com.fridge.fridge_server.domain.food.dto.UpdateFoodRequest
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

import com.fridge.fridge_server.domain.auth.CurrentUser.CurrentUser
import com.fridge.fridge_server.domain.auth.UserPrincipal
import io.swagger.v3.oas.annotations.Parameter

@RestController
@RequestMapping("/foods")
@SecurityRequirement(name = "bearerAuth")
class FoodEndpoint(
    private val foodService: FoodService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createFood(@RequestBody request: CreateFoodRequest) : Food{
        return foodService.createFood(request)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getFoods(@RequestParam fridgeId: Long): List<FoodResponse> {
        return foodService.getFoodsByFridge(fridgeId).map { FoodResponse.from(it) }
    }

    @GetMapping("/group")
    @ResponseStatus(HttpStatus.OK)
    fun getFoodsByGroup(@Parameter(hidden = true) @CurrentUser user: UserPrincipal): List<FoodResponse> {
        val groupId = user.getFamilyId()
        val foods = foodService.getFoodsByFamilyGroup(groupId)
        return foods.map { FoodResponse.from(it) }
    }

    @PutMapping("/{foodId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateFood(
        @PathVariable foodId: Long,
        @RequestBody request: UpdateFoodRequest
    ): FoodResponse {
        val updated = foodService.updateFood(foodId, request)
        return FoodResponse.from(updated)
    }

    @DeleteMapping("/{foodId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteFood(@PathVariable foodId: Long) {
        foodService.deleteFood(foodId)
    }
}