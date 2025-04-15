package com.fridge.fridge_server.domain.fridge

import com.fridge.fridge_server.domain.family.FamilyGroup
import org.springframework.data.jpa.repository.JpaRepository

interface FridgeRepository : JpaRepository<Fridge, Long> {
    fun findAllByFamilyGroup(familyGroup: FamilyGroup): List<Fridge>
}