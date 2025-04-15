package com.fridge.fridge_server.domain.family

import org.springframework.stereotype.Service

@Service
class FamilyGroupService(
    private val familyGroupRepository: FamilyGroupRepository
) {
    fun createDefaultGroupForUser(userName: String): FamilyGroup {
        val family = FamilyGroup(name = "${userName}의 가족")
        return familyGroupRepository.save(family)
    }
}