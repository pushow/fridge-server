package com.fridge.fridge_server.domain.familyinvite

import com.fridge.fridge_server.domain.family.FamilyGroup
import com.fridge.fridge_server.domain.familyinvite.dto.InviteStatus
import com.fridge.fridge_server.domain.user.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "family_invites")
class FamilyInvite(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val inviterName: String, // 초대한 사람 이름 (기록용)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_family_id", nullable = false)
    val fromFamily: FamilyGroup,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id", nullable = false)
    val toUser: User,

    @Enumerated(EnumType.STRING)
    var status: InviteStatus = InviteStatus.PENDING,

    val invitedAt: LocalDateTime = LocalDateTime.now()
)