package com.fridge.fridge_server.domain.user

import com.fridge.fridge_server.domain.family.FamilyGroupService
import com.fridge.fridge_server.domain.fridge.FridgeService
import com.fridge.fridge_server.domain.user.dto.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.fridge.fridge_server.common.CustomException
import com.fridge.fridge_server.common.ErrorCode

interface UserUseCase {
    fun isEmailAvailable(email: String): Boolean
    fun createUser(dto: CreateUserRequest):User
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
    @Transactional(readOnly = true)
    override fun isEmailAvailable(email: String): Boolean {
        if (userRepository.existsByEmail(email)) {
            throw CustomException(ErrorCode.EMAIL_ALREADY_EXISTS)
        }
        else return true;
    }

    @Transactional
    override fun createUser(dto: CreateUserRequest) : User {
        isEmailAvailable(dto.email)
        val encodedPassword = passwordEncoder.encode(dto.password)
        val family = familyGroupService.createDefaultGroupForUser(dto.name)

        val user = User(
            name = dto.name,
            email = dto.email,
            password = encodedPassword,
            familyGroup = family
        )

        return userRepository.save(user)
    }

    @Transactional(readOnly = true)
    override fun getUserInfo(userId: Long): UserInfoResponse {
        val user = userRepository.findById(userId)
            .orElseThrow {  CustomException(ErrorCode.USER_NOT_FOUND) }

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
            .orElseThrow { CustomException(ErrorCode.USER_NOT_FOUND) }

        user.name = request.name

        return user
    }

    @Transactional
    override fun deleteUser(userId: Long) {
        val user = userRepository.findById(userId)
            .orElseThrow { CustomException(ErrorCode.USER_NOT_FOUND) }

        userRepository.delete(user)
    }

}