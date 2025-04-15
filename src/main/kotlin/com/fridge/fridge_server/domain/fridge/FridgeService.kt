package com.fridge.fridge_server.domain.fridge

import com.fridge.fridge_server.domain.family.FamilyGroup
import org.springframework.stereotype.Service

@Service
class FridgeService(private val fridgeRepository: FridgeRepository) {
    fun getFridgesByFamilyGroup(familyGroup: FamilyGroup): List<Fridge> {
        return fridgeRepository.findAllByFamilyGroup(familyGroup)
    }
}