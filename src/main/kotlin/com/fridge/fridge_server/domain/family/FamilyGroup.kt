package com.fridge.fridge_server.domain.family

import com.fridge.fridge_server.domain.user.User
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
class FamilyGroup(
    @Id @GeneratedValue val id: Long = 0,
    val name: String,

    @OneToMany(mappedBy = "familyGroup", cascade = [CascadeType.ALL])
    val users: List<User> = mutableListOf(),

    @OneToMany(mappedBy = "familyGroup", cascade = [CascadeType.ALL])
    val fridges: List<Fridge> = mutableListOf()
)

@Entity
class Fridge(
    @Id @GeneratedValue val id: Long = 0,
    val name: String,

    @ManyToOne
    @JoinColumn(name = "family_group_id")
    val familyGroup: FamilyGroup
)
