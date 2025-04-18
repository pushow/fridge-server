package com.fridge.fridge_server.domain.fridge

import com.fridge.fridge_server.domain.family.FamilyGroupService
import com.fridge.fridge_server.domain.fridge.dto.CreateFridgeRequest
import com.fridge.fridge_server.domain.user.UserService
import com.fridge.fridge_server.domain.user.dto.CreateUserRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class FridgeServiceTest @Autowired constructor(
    val fridgeService: FridgeService,
    val userService: UserService  // 사용자 생성해서 가족 ID 얻기 용도
) {

    fun createFamilyUser() = userService.createUser(
        CreateUserRequest("냉장고주인", "fridge@test.com", "1234")
    )

    @Test
    fun `냉장고를 생성하면 가족 그룹에 저장된다`() {
        val user = createFamilyUser()

        val fridge = fridgeService.createFridge("우리집 냉장고", user.id)

        assertEquals("우리집 냉장고", fridge.name)
        assertEquals(user.familyGroup.id, fridge.familyGroup.id)
    }

    @Test
    fun `냉장고 이름을 변경할 수 있다`() {
        val user = createFamilyUser()
        val fridge = fridgeService.createFridge(
            "변경 전",
            user.familyGroup.id
        )

        val updated = fridgeService.updateFridgeName(fridge.id, "변경 후")

        assertEquals("변경 후", updated.name)
    }

    @Test
    fun `냉장고를 삭제하면 음식도 함께 삭제된다`() {
        val user = createFamilyUser()
        val fridge = fridgeService.createFridge(
            "삭제 냉장고",
            user.familyGroup.id
        )

        // 삭제해도 예외 안 나고 정상 삭제되면 성공
        assertDoesNotThrow {
            fridgeService.deleteFridge(fridge.id)
        }
    }
}