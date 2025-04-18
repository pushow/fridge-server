package com.fridge.fridge_server.domain.familyinvite

import com.fridge.fridge_server.domain.family.FamilyGroupRepository
import com.fridge.fridge_server.domain.familyinvite.dto.InviteStatus
import com.fridge.fridge_server.domain.user.UserRepository
import org.springframework.stereotype.Service
import com.fridge.fridge_server.common.CustomException
import com.fridge.fridge_server.common.ErrorCode


interface FamilyInviteUseCase {
    fun sendInvite(fromFamilyId: Long, inviterName: String, inviterEmail: String, toUserEmail: String) :FamilyInvite
    fun getPendingInvitesForUser(userId: Long): List<FamilyInvite>
    fun acceptInvite(invitationId: Long, userId: Long)
    fun declineInvite(invitationId: Long, userId: Long)
}
@Service
class FamilyInviteService(
    private val inviteRepository: FamilyInviteRepository,
    private val userRepository: UserRepository,
    private val familyGroupRepository: FamilyGroupRepository
) : FamilyInviteUseCase {

    override fun sendInvite(fromFamilyId: Long, inviterName: String, inviterEmail: String,toUserEmail: String): FamilyInvite{
        val family = findFamilyOrThrow(fromFamilyId)
        val toUser = findUserByEmailOrThrow(toUserEmail)

        if (inviteRepository.existsByToUserAndStatus(toUser, InviteStatus.PENDING)) {
            throw CustomException(ErrorCode.INVITE_ALREADY_EXISTS)
        }

        val invite = FamilyInvite(
            inviterName = inviterName,
            inviterEmail = inviterEmail,
            fromFamily = family,
            toUser = toUser
        )

        return inviteRepository.save(invite)
    }

    override fun getPendingInvitesForUser(userId: Long): List<FamilyInvite> {
        val user = findUserOrThrow(userId)
        return inviteRepository.findAllByToUserAndStatus(user, InviteStatus.PENDING)
    }

    override fun acceptInvite(invitationId: Long, userId: Long) {
        val user = findUserOrThrow(userId)
        val invite = inviteRepository.findByIdAndToUser(invitationId, user)
            ?: throw CustomException(ErrorCode.INVITE_NOT_FOUND)

        val oldFamily = user.familyGroup
        val newFamily = invite.fromFamily

        user.changeFamilyGroup(newFamily)
        invite.status = InviteStatus.ACCEPTED
        inviteRepository.save(invite)

        val others = userRepository.findAll().filter { it.familyGroup == oldFamily && it.id != user.id }
        if (others.isEmpty()) {
            familyGroupRepository.delete(oldFamily)
        }
    }

    override fun declineInvite(invitationId: Long, userId: Long) {
        val user = findUserOrThrow(userId)
        val invite = inviteRepository.findByIdAndToUser(invitationId, user)
            ?: throw CustomException(ErrorCode.INVITE_NOT_FOUND)

        invite.status = InviteStatus.DECLINED
        inviteRepository.save(invite)
    }

    private fun findUserOrThrow(userId: Long) =
        userRepository.findById(userId).orElseThrow { CustomException(ErrorCode.USER_NOT_FOUND) }

    private fun findUserByEmailOrThrow(userEmail: String) =
        userRepository.findByEmail(userEmail) ?: throw CustomException(ErrorCode.USER_NOT_FOUND)


    private fun findFamilyOrThrow(familyId: Long) =
        familyGroupRepository.findById(familyId).orElseThrow { CustomException(ErrorCode.FAMILY_NOT_FOUND) }
}
