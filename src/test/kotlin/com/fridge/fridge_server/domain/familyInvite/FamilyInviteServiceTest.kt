package com.fridge.fridge_server.domain.familyInvite

import com.fridge.fridge_server.domain.family.FamilyGroupRepository
import com.fridge.fridge_server.domain.familyinvite.FamilyInviteRepository
import com.fridge.fridge_server.domain.familyinvite.FamilyInviteService
import com.fridge.fridge_server.domain.user.UserService
import com.fridge.fridge_server.domain.user.UserRepository
import com.fridge.fridge_server.domain.user.dto.CreateUserRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import com.fridge.fridge_server.domain.familyinvite.dto.InviteStatus

@SpringBootTest
@Transactional
class FamilyInviteServiceTest @Autowired constructor(
    val familyInviteService: FamilyInviteService,
    val userService: UserService,
    val userRepository: UserRepository,
    val familyGroupRepository: FamilyGroupRepository,
    val familyInviteRepository: FamilyInviteRepository
) {

    fun createUser(name: String, email: String): com.fridge.fridge_server.domain.user.User {
        return userService.createUser(CreateUserRequest(name, email, "pass"))
    }

    @Test
    fun `초대를 전송하면 PENDING 상태로 저장된다`() {
        val from = createUser("보내는 사람", "from@test.com")
        val to = createUser("받는 사람", "to@test.com")

        familyInviteService.sendInvite(
            fromFamilyId = from.familyGroup.id,
            inviterName = from.name,
            toUserId = to.id
        )

        val invites = familyInviteRepository.findAllByToUserAndStatus(to, InviteStatus.PENDING)
        assertEquals(1, invites.size)
        assertEquals(from.familyGroup.id, invites[0].fromFamily.id)
        assertEquals(InviteStatus.PENDING, invites[0].status)
    }

    @Test
    fun `초대 수락 시 유저는 초대한 가족으로 이동하고 기존 가족이 삭제된다`() {
        val from = createUser("초대자", "from2@test.com")
        val to = createUser("수락자", "to2@test.com")
        val oldFamilyId = to.familyGroup.id

        familyInviteService.sendInvite(from.familyGroup.id, from.name, to.id)
        val invite = familyInviteRepository.findAllByToUserAndStatus(to, InviteStatus.PENDING).first()

        familyInviteService.acceptInvite(invite.id, to.id)

        val updatedUser = userRepository.findById(to.id).get()
        assertEquals(from.familyGroup.id, updatedUser.familyGroup.id)
        assertFalse(familyGroupRepository.existsById(oldFamilyId))

        val updatedInvite = familyInviteRepository.findById(invite.id).get()
        assertEquals(InviteStatus.ACCEPTED, updatedInvite.status)
    }

    @Test
    fun `초대를 거절하면 상태가 DECLINED로 변경된다`() {
        val from = createUser("초대자", "from3@test.com")
        val to = createUser("거절자", "to3@test.com")

        familyInviteService.sendInvite(from.familyGroup.id, from.name, to.id)
        val invite = familyInviteRepository.findAllByToUserAndStatus(to, InviteStatus.PENDING).first()

        familyInviteService.declineInvite(invite.id, to.id)

        val declined = familyInviteRepository.findById(invite.id).get()
        assertEquals(InviteStatus.DECLINED, declined.status)
    }

    @Test
    fun `이미 초대가 존재하면 중복 초대가 불가능하다`() {
        val from = createUser("초대자", "from4@test.com")
        val to = createUser("중복자", "to4@test.com")

        familyInviteService.sendInvite(from.familyGroup.id, from.name, to.id)

        val exception = assertThrows(IllegalStateException::class.java) {
            familyInviteService.sendInvite(from.familyGroup.id, from.name, to.id)
        }

        assertEquals("이미 초대가 존재합니다.", exception.message)
    }
}