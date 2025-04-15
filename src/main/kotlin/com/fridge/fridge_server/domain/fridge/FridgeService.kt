package com.fridge.fridge_server.domain.fridge

import com.fridge.fridge_server.domain.family.FamilyGroup
import com.fridge.fridge_server.domain.family.FamilyGroupRepository
import com.fridge.fridge_server.domain.food.FoodRepository
import com.fridge.fridge_server.domain.fridge.dto.CreateFridgeRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FridgeService(
    private val fridgeRepository: FridgeRepository,
    private val foodRepository: FoodRepository,
    private val familyGroupRepository: FamilyGroupRepository
) {
    @Transactional(readOnly = true)
    fun getFridgesByFamilyGroup(familyGroup: FamilyGroup): List<Fridge> {
        return fridgeRepository.findAllByFamilyGroup(familyGroup)
    }

    @Transactional
    fun createFridge(request: CreateFridgeRequest): Fridge {
        val family = familyGroupRepository.findById(request.familyGroupId)
            .orElseThrow { IllegalArgumentException("가족 그룹 없음") }

        val fridge = Fridge(name = request.name, familyGroup = family)
        return fridgeRepository.save(fridge)
    }

    @Transactional
    fun updateFridgeName(fridgeId: Long, name: String): Fridge {
        val fridge = fridgeRepository.findById(fridgeId)
            .orElseThrow { IllegalArgumentException("냉장고 없음") }

        fridge.name = name
        return fridge
    }

    @Transactional
    fun deleteFridge(fridgeId: Long) {
        val fridge = fridgeRepository.findById(fridgeId)
            .orElseThrow { IllegalArgumentException("냉장고 없음") }

        // 1. 음식 먼저 삭제
        val foods = foodRepository.findAllByFridge(fridge)
        foodRepository.deleteAll(foods)

        // 2. 냉장고 삭제
        fridgeRepository.delete(fridge)
    }
}