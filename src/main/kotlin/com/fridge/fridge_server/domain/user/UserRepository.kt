package com.fridge.fridge_server.domain.user

import com.fridge.fridge_server.domain.family.FamilyGroup
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun findAllByFamilyGroup(familyGroup: FamilyGroup): List<User>
}