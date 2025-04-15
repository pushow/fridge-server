package com.fridge.fridge_server.domain.user

import com.fridge.fridge_server.domain.user.dto.CreateUserRequest
import com.fridge.fridge_server.domain.family.FamilyGroupRepository
import com.fridge.fridge_server.domain.fridge.FridgeService
import com.fridge.fridge_server.domain.user.dto.UserInfoResponse
import com.fridge.fridge_server.domain.user.dto.UserLoginRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class UserServiceTest @Autowired constructor(
    val userService: UserService,
    val userRepository: UserRepository,
    val familyGroupRepository: FamilyGroupRepository
) {

    @Autowired
    private lateinit var fridgeService: FridgeService

    @Test
    fun `유저 생성시 유저와 가족 그룹이 저장된다`() {
        // given
        val request = CreateUserRequest(
            name = "테스트유저",
            email = "test@example.com",
            password = "password123"
        )

        // when
        val user = userService.createUser(request)

        // then
        assertEquals(request.name, user.name)
        assertEquals(request.email, user.email)
        assertNotNull(user.familyGroup)
        assertTrue(familyGroupRepository.existsById(user.familyGroup.id))
    }

    @Test
    fun `이미 존재하는 이메일이면 예외가 발생한다`() {
        // given
        val request = CreateUserRequest(
            name = "중복유저",
            email = "dup@example.com",
            password = "1234"
        )

        userService.createUser(request)

        // when + then
        val exception = assertThrows(IllegalArgumentException::class.java) {
            userService.createUser(request)
        }

        assertEquals("이미 사용 중인 이메일입니다.", exception.message)
    }

    @Test
    fun `정상적으로 로그인하면 유저를 반환한다`() {
        // given
        val request = CreateUserRequest("테스트", "login@test.com", "pass123")
        userService.createUser(request)

        val loginRequest = UserLoginRequest("login@test.com", "pass123")

        // when
        val result = userService.login(loginRequest)

        // then
        assertEquals("테스트", result.name)
        assertEquals("login@test.com", result.email)
    }

    @Test
    fun `없는 이메일로 로그인하면 예외가 발생한다`() {
        // given
        val loginRequest = UserLoginRequest("nonexist@test.com", "whatever")

        // when + then
        val exception = assertThrows(IllegalArgumentException::class.java) {
            userService.login(loginRequest)
        }

        assertEquals("존재하지 않는 이메일입니다.", exception.message)
    }

    @Test
    fun `비밀번호가 틀리면 예외가 발생한다`() {
        // given
        val request = CreateUserRequest("테스트", "wrongpass@test.com", "correct123")
        userService.createUser(request)

        val loginRequest = UserLoginRequest("wrongpass@test.com", "wrong123")

        // when + then
        val exception = assertThrows(IllegalArgumentException::class.java) {
            userService.login(loginRequest)
        }

        assertEquals("비밀번호가 일치하지 않습니다.", exception.message)
    }

    @Test
    fun `유저 정보 조회시 가족과 냉장고 정보도 함께 반환된다`() {
        // given
        val request = CreateUserRequest("정보유저", "info@test.com", "pass123")
        val user = userService.createUser(request)

        // 냉장고 하나 추가 (fridgeService 직접 사용)
        fridgeService.createFridge(
            com.fridge.fridge_server.domain.fridge.dto.CreateFridgeRequest(
                familyGroupId = user.familyGroup.id,
                name = "테스트 냉장고"
            )
        )

        // when
        val info: UserInfoResponse = userService.getUserInfo(user.id)

        // then
        assertEquals(user.id, info.userId)
        assertEquals(user.name, info.userName)
        assertEquals(user.email, info.email)
        assertEquals(user.familyGroup.id, info.familyGroupId)
        assertEquals(user.familyGroup.name, info.familyGroupName)
        assertEquals(1, info.fridges.size)
        assertEquals("테스트 냉장고", info.fridges.first().name)
    }
}