package com.fridge.fridge_server.domain.user

import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val familyGroupRepository: FamilyGroupRepository
) {
    fun registerUser(name: String, email: String, password: String): User {
        val user = User(name = name, email = email, password = encode(password))
        return userRepository.save(user)
    }

    fun joinFamilyGroup(userId: Long, familyGroupId: Long) {
        val user = userRepository.findById(userId).orElseThrow()
        val group = familyGroupRepository.findById(familyGroupId).orElseThrow()
        val updated = user.copy(familyGroup = group)
        userRepository.save(updated)
    }

    private fun encode(raw: String): String {
        // 비밀번호 해싱 로직 (예: BCrypt)
        return raw // 임시
    }
}