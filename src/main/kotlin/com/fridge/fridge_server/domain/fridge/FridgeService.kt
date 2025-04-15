package com.fridge.fridge_server.domain.fridge

import com.fridge.fridge_server.domain.family.FamilyGroup
import com.fridge.fridge_server.domain.family.FamilyGroupRepository
import com.fridge.fridge_server.domain.food.FoodRepository
import com.fridge.fridge_server.domain.fridge.dto.CreateFridgeRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface FridgeUseCase {
    fun getFridgesByFamilyGroup(familyGroup: FamilyGroup): List<Fridge>
    fun createFridge(request: CreateFridgeRequest): Fridge
    fun updateFridgeName(fridgeId: Long, name: String): Fridge
    fun deleteFridge(fridgeId: Long)
}

@Service
class FridgeService(
    private val fridgeRepository: FridgeRepository,
    private val foodRepository: FoodRepository,
    private val familyGroupRepository: FamilyGroupRepository
):FridgeUseCase {
    @Transactional(readOnly = true)
    override fun getFridgesByFamilyGroup(familyGroup: FamilyGroup): List<Fridge> {
        return fridgeRepository.findAllByFamilyGroup(familyGroup)
    }

    @Transactional
    override fun createFridge(request: CreateFridgeRequest): Fridge {
        val family = familyGroupRepository.findById(request.familyGroupId)
            .orElseThrow { IllegalArgumentException("가족 그룹 없음") }

        val fridge = Fridge(name = request.name, familyGroup = family)
        return fridgeRepository.save(fridge)
    }

    @Transactional
    override fun updateFridgeName(fridgeId: Long, name: String): Fridge {
        val fridge = fridgeRepository.findById(fridgeId)
            .orElseThrow { IllegalArgumentException("냉장고 없음") }

        fridge.name = name
        return fridge
    }

    @Transactional
    override fun deleteFridge(fridgeId: Long) {
        val fridge = fridgeRepository.findById(fridgeId)
            .orElseThrow { IllegalArgumentException("냉장고 없음") }

        // 1. 음식 먼저 삭제
        val foods = foodRepository.findAllByFridge(fridge)
        foodRepository.deleteAll(foods)

        // 2. 냉장고 삭제
        fridgeRepository.delete(fridge)
    }
}