package com.fridge.fridge_server.domain.family

import com.fridge.fridge_server.domain.user.User
import com.fridge.fridge_server.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface FamilyGroupUseCase {
    fun getMembers(familyGroupId: Long): List<User>
    fun createDefaultGroupForUser(userName: String): FamilyGroup
    fun updateFamilyGroupName(familyId: Long, newName: String): FamilyGroup
    fun leaveFamilyGroup(userId: Long)
}

@Service
class FamilyGroupService(
    private val familyGroupRepository: FamilyGroupRepository,
    private val userRepository: UserRepository
):FamilyGroupUseCase {
    @Transactional(readOnly = true)
    override fun getMembers(familyGroupId: Long): List<User> {
        val family = familyGroupRepository.findById(familyGroupId)
            .orElseThrow { IllegalArgumentException("가족 없음") }

        return userRepository.findAll().filter { it.familyGroup == family }
    }

    @Transactional
    override fun createDefaultGroupForUser(userName: String): FamilyGroup {
        val family = FamilyGroup(name = "${userName}의 가족")
        return familyGroupRepository.save(family)
    }

    @Transactional
    override fun updateFamilyGroupName(familyId: Long, newName: String): FamilyGroup {
        val family = familyGroupRepository.findById(familyId)
            .orElseThrow { IllegalArgumentException("가족 그룹 없음") }

        family.name = newName
        return family
    }

    @Transactional
    override fun leaveFamilyGroup(userId: Long) {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("유저 없음") }

        val oldFamily = user.familyGroup
        val remaining = userRepository.findAll()
            .filter { it.familyGroup == oldFamily && it.id != user.id }

        // 새로운 가족 생성 및 이동
        val newFamily = createDefaultGroupForUser(user.name)
        user.changeFamilyGroup(newFamily)

        userRepository.save(user)

        if (remaining.isEmpty()) {
            familyGroupRepository.delete(oldFamily)
        }
    }
}