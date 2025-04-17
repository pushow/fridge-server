package com.fridge.fridge_server.domain.fridge

import com.fridge.fridge_server.domain.family.FamilyGroup
import com.fridge.fridge_server.domain.family.FamilyGroupRepository
import com.fridge.fridge_server.domain.food.FoodRepository
import com.fridge.fridge_server.domain.fridge.dto.CreateFridgeRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.fridge.fridge_server.common.CustomException
import com.fridge.fridge_server.common.ErrorCode



interface FridgeUseCase {
    fun getFridgesByFamilyGroup(familyGroup: FamilyGroup): List<Fridge>
    fun createFridge(name: String, familyId : Long): Fridge
    fun updateFridgeName(fridgeId: Long, name: String): Fridge
    fun deleteFridge(fridgeId: Long)
}

@Service
class FridgeService(
    private val fridgeRepository: FridgeRepository,
    private val foodRepository: FoodRepository,
    private val familyGroupRepository: FamilyGroupRepository
) : FridgeUseCase {

    @Transactional(readOnly = true)
    override fun getFridgesByFamilyGroup(familyGroup: FamilyGroup): List<Fridge> {
        return fridgeRepository.findAllByFamilyGroup(familyGroup)
    }

    @Transactional
    override fun createFridge(name: String, familyId: Long): Fridge {
        val family = findFamilyOrThrow(familyId)
        val fridge = Fridge(name = name, familyGroup = family)
        return fridgeRepository.save(fridge)
    }

    @Transactional
    override fun updateFridgeName(fridgeId: Long, name: String): Fridge {
        val fridge = findFridgeOrThrow(fridgeId)
        fridge.name = name
        return fridge
    }

    @Transactional
    override fun deleteFridge(fridgeId: Long) {
        val fridge = findFridgeOrThrow(fridgeId)

        // 1. 음식 먼저 삭제
        val foods = foodRepository.findAllByFridge(fridge)
        foodRepository.deleteAll(foods)

        // 2. 냉장고 삭제
        fridgeRepository.delete(fridge)
    }

    private fun findFridgeOrThrow(fridgeId: Long): Fridge =
        fridgeRepository.findById(fridgeId)
            .orElseThrow { CustomException(ErrorCode.FRIDGE_NOT_FOUND) }

    private fun findFamilyOrThrow(familyId: Long): FamilyGroup =
        familyGroupRepository.findById(familyId)
            .orElseThrow { CustomException(ErrorCode.FAMILY_NOT_FOUND) }
}