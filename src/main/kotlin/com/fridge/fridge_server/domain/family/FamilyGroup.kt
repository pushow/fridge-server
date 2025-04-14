package com.fridge.fridge_server.domain.family

import com.fridge.fridge_server.domain.fridge.Fridge
import com.fridge.fridge_server.domain.user.User
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
class FamilyGroup(
    @Id @GeneratedValue val id: Long = 0,
    val name: String,

    @OneToMany(mappedBy = "familyGroup", cascade = [CascadeType.ALL], orphanRemoval = true)
    val users: List<User> = mutableListOf(),

    @OneToMany(mappedBy = "familyGroup", cascade = [CascadeType.ALL], orphanRemoval = true)
    val fridges: List<Fridge> = mutableListOf()
)