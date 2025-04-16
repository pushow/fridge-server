package com.fridge.fridge_server.endpoint

import com.fridge.fridge_server.domain.fridge.FridgeService
import com.fridge.fridge_server.domain.fridge.dto.CreateFridgeRequest
import com.fridge.fridge_server.domain.fridge.dto.FridgeResponse
import com.fridge.fridge_server.domain.fridge.dto.UpdateFridgeRequest
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/fridges")
@SecurityRequirement(name = "bearerAuth")
class FridgeEndpoint(
    private val fridgeService: FridgeService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createFridge(@RequestBody request: CreateFridgeRequest): FridgeResponse {
        val fridge = fridgeService.createFridge(request)
        return FridgeResponse.from(fridge)
    }

    @PutMapping("/{fridgeId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateFridge(@PathVariable fridgeId: Long, @RequestBody request: UpdateFridgeRequest): FridgeResponse {
        val updated = fridgeService.updateFridgeName(fridgeId, request.name)
        return FridgeResponse.from(updated)
    }

    @DeleteMapping("/{fridgeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteFridge(@PathVariable fridgeId: Long) {
        fridgeService.deleteFridge(fridgeId)
    }
}