package com.fridge.fridge_server.domain.family

import com.fridge.fridge_server.domain.user.User
import com.fridge.fridge_server.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.fridge.fridge_server.common.CustomException
import com.fridge.fridge_server.common.ErrorCode


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
) : FamilyGroupUseCase {

    @Transactional(readOnly = true)
    override fun getMembers(familyGroupId: Long): List<User> {
        val family = findFamilyOrThrow(familyGroupId)
        return userRepository.findAll().filter { it.familyGroup == family }
    }

    @Transactional
    override fun createDefaultGroupForUser(userName: String): FamilyGroup {
        val family = FamilyGroup(name = "${userName}의 가족")
        return familyGroupRepository.save(family)
    }

    @Transactional
    override fun updateFamilyGroupName(familyId: Long, newName: String): FamilyGroup {
        val family = findFamilyOrThrow(familyId)
        family.name = newName
        return family
    }

    @Transactional
    override fun leaveFamilyGroup(userId: Long) {
        val user = findUserOrThrow(userId)
        val oldFamily = user.familyGroup
        val remaining = userRepository.findAll().filter { it.familyGroup == oldFamily && it.id != user.id }

        val newFamily = createDefaultGroupForUser(user.name)
        user.changeFamilyGroup(newFamily)
        userRepository.save(user)

        if (remaining.isEmpty()) {
            familyGroupRepository.delete(oldFamily)
        }
    }

    private fun findFamilyOrThrow(familyId: Long) =
        familyGroupRepository.findById(familyId)
            .orElseThrow { CustomException(ErrorCode.FAMILY_NOT_FOUND) }

    private fun findUserOrThrow(userId: Long) =
        userRepository.findById(userId)
            .orElseThrow { CustomException(ErrorCode.USER_NOT_FOUND) }
}