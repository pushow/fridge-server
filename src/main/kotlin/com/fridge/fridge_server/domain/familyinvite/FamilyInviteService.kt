package com.fridge.fridge_server.domain.familyinvite

import com.fridge.fridge_server.domain.family.FamilyGroupRepository
import com.fridge.fridge_server.domain.user.UserRepository
import org.springframework.stereotype.Service

interface FamilyInviteUseCase {
    fun sendInvite(fromFamilyId: Long, inviterName: String, toUserId: Long)
    fun getPendingInvitesForUser(userId: Long): List<FamilyInvite>
    fun acceptInvite(invitationId: Long, userId: Long)
    fun declineInvite(invitationId: Long, userId: Long)
}
@Service
class FamilyInviteService(
    private val inviteRepository: FamilyInviteRepository,
    private val userRepository: UserRepository,
    private val familyGroupRepository: FamilyGroupRepository
):FamilyInviteUseCase {
    override fun sendInvite(fromFamilyId: Long, inviterName: String, toUserId: Long) {
        val family = familyGroupRepository.findById(fromFamilyId)
            .orElseThrow { IllegalArgumentException("가족 그룹 없음") }

        val toUser = userRepository.findById(toUserId)
            .orElseThrow { IllegalArgumentException("유저 없음") }

        if (inviteRepository.existsByToUserAndStatus(toUser, InviteStatus.PENDING)) {
            throw IllegalStateException("이미 초대가 존재합니다.")
        }

        val invite = FamilyInvite(
            inviterName = inviterName,
            fromFamily = family,
            toUser = toUser
        )

        inviteRepository.save(invite)
    }

    override fun getPendingInvitesForUser(userId: Long): List<FamilyInvite> {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("유저 없음") }

        return inviteRepository.findAllByToUserAndStatus(user, InviteStatus.PENDING)
    }

    override fun acceptInvite(invitationId: Long, userId: Long) {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("유저 없음") }

        val invite = inviteRepository.findByIdAndToUser(invitationId, user)
            ?: throw IllegalArgumentException("해당 초대가 없습니다.")

        val oldFamily = user.familyGroup
        val newFamily = invite.fromFamily

        user.changeFamilyGroup(newFamily) // User 엔티티에 해당 메서드 필요
        // userRepository.save(user)

        invite.status = InviteStatus.ACCEPTED
        inviteRepository.save(invite)

        // 기존 가족 구성원이 없다면 삭제
        val others = userRepository.findAll().filter { it.familyGroup == oldFamily && it.id != user.id }
        if (others.isEmpty()) {
            familyGroupRepository.delete(oldFamily)
        }
    }

    override fun declineInvite(invitationId: Long, userId: Long) {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("유저 없음") }

        val invite = inviteRepository.findByIdAndToUser(invitationId, user)
            ?: throw IllegalArgumentException("해당 초대가 없습니다.")

        invite.status = InviteStatus.DECLINED
        inviteRepository.save(invite)
    }
}
