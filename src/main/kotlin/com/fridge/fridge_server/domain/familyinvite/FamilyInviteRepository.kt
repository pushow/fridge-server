package com.fridge.fridge_server.domain.familyinvite

import com.fridge.fridge_server.domain.familyinvite.dto.InviteStatus
import com.fridge.fridge_server.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository

interface FamilyInviteRepository : JpaRepository<FamilyInvite, Long> {

    fun findAllByToUserAndStatus(toUser: User, status: InviteStatus): List<FamilyInvite>

    fun existsByToUserAndStatus(toUser: User, status: InviteStatus): Boolean

    fun existsByToUserAndStatusNot(toUser: User, status: InviteStatus): Boolean

    fun findByIdAndToUser(id: Long, toUser: User): FamilyInvite?
}