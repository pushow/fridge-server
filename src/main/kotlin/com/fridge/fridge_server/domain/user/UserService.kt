package com.fridge.fridge_server.domain.user

import com.fridge.fridge_server.domain.family.FamilyGroupService
import com.fridge.fridge_server.domain.fridge.FridgeService
import com.fridge.fridge_server.domain.user.dto.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


interface UserUseCase {
    fun createUser(dto: CreateUserRequest)
    fun login(dto: UserLoginRequest): User
    fun getUserInfo(userId: Long): UserInfoResponse
    fun updateUser(userId: Long, request: UpdateUserRequest): User
    fun deleteUser(userId: Long)
}

@Service
class UserService(
    private val userRepository: UserRepository,
    private val familyGroupService: FamilyGroupService,
    private val fridgeService: FridgeService,
    private val passwordEncoder: PasswordEncoder
) :UserUseCase{
    @Transactional
    override fun createUser(dto: CreateUserRequest) {
        if (userRepository.existsByEmail(dto.email)) {
            throw IllegalArgumentException("이미 사용 중인 이메일입니다.")
        }
        val encodedPassword = passwordEncoder.encode(dto.password)
        val family = familyGroupService.createDefaultGroupForUser(dto.name)

        val user = User(
            name = dto.name,
            email = dto.email,
            password = encodedPassword,
            familyGroup = family
        )

        userRepository.save(user)
    }

    @Transactional(readOnly = true)
    override fun login(dto: UserLoginRequest): User {
        val user = userRepository.findByEmail(dto.email)
            ?: throw IllegalArgumentException("존재하지 않는 이메일입니다.")

        if (user.password != dto.password) {
            throw IllegalArgumentException("비밀번호가 일치하지 않습니다.")
        }

        return user
    }

    @Transactional(readOnly = true)
    override fun getUserInfo(userId: Long): UserInfoResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("유저 없음") }

        val family = user.familyGroup

        val fridges = fridgeService.getFridgesByFamilyGroup(family)

        return UserInfoResponse(
            userId = user.id,
            userName = user.name,
            email = user.email,
            familyGroupId = family.id,
            familyGroupName = family.name,
            fridges = fridges.map { UserFridgeInfo(it.id, it.name) }
        )
    }

    @Transactional
    override fun updateUser(userId: Long, request: UpdateUserRequest): User {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("유저 없음") }

        user.name = request.name

        return user
    }

    @Transactional
    override fun deleteUser(userId: Long) {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("유저 없음") }

        userRepository.delete(user)
    }

}