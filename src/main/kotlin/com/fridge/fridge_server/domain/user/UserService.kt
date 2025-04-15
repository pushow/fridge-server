package com.fridge.fridge_server.domain.user

import com.fridge.fridge_server.domain.family.FamilyGroupService
import com.fridge.fridge_server.domain.user.dto.CreateUserRequest
import com.fridge.fridge_server.domain.user.dto.UserLoginRequest
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val familyGroupService: FamilyGroupService
) {
    fun createUser(dto: CreateUserRequest): User {
        if (userRepository.existsByEmail(dto.email)) {
            throw IllegalArgumentException("이미 사용 중인 이메일입니다.")
        }

        val family = familyGroupService.createDefaultGroupForUser(dto.name)

        val user = User(
            name = dto.name,
            email = dto.email,
            password = dto.password,
            familyGroup = family
        )

        return userRepository.save(user)
    }

    fun login(dto: UserLoginRequest): User {
        val user = userRepository.findByEmail(dto.email)
            ?: throw IllegalArgumentException("존재하지 않는 이메일입니다.")

        if (user.password != dto.password) {
            throw IllegalArgumentException("비밀번호가 일치하지 않습니다.")
        }

        return user
    }


}