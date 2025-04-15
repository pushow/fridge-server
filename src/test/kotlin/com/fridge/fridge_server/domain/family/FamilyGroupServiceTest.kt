package com.fridge.fridge_server.domain.family

import com.fridge.fridge_server.domain.user.UserRepository
import com.fridge.fridge_server.domain.user.UserService
import com.fridge.fridge_server.domain.user.dto.CreateUserRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class FamilyGroupServiceTest @Autowired constructor(
    val familyGroupService: FamilyGroupService,
    val userService: UserService,
    val userRepository: UserRepository,
    val familyGroupRepository: FamilyGroupRepository
) {

    fun createUser(): com.fridge.fridge_server.domain.user.User {
        return userService.createUser(
            CreateUserRequest("탈퇴유저", "leave@test.com", "pass")
        )
    }

    @Test
    fun `가족 이름을 수정하면 변경된 이름이 반영된다`() {
        val user = createUser()
        val updated = familyGroupService.updateFamilyGroupName(user.familyGroup.id, "새로운 가족")

        assertEquals("새로운 가족", updated.name)
    }

    @Test
    fun `가족을 탈퇴하면 새 가족이 생성되고, 기존 가족은 사람이 없으면 삭제된다`() {
        val user = createUser()
        val oldFamilyId = user.familyGroup.id

        familyGroupService.leaveFamilyGroup(user.id)

        val refreshedUser = userRepository.findById(user.id).get()
        assertNotEquals(oldFamilyId, refreshedUser.familyGroup.id)
        assertFalse(familyGroupRepository.existsById(oldFamilyId))
    }
}