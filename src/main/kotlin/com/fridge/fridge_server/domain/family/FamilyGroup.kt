package com.fridge.fridge_server.domain.family

import com.fridge.fridge_server.domain.fridge.Fridge
import com.fridge.fridge_server.domain.user.User
import jakarta.persistence.*


@Entity
@Table(name = "family_group")
class FamilyGroup(
    @Id @GeneratedValue val id: Long = 0,
    var name: String,

    @OneToMany(mappedBy = "familyGroup")
    val users: MutableList<User> = mutableListOf(),

    @OneToMany(mappedBy = "familyGroup", cascade = [CascadeType.ALL], orphanRemoval = true)
    val fridges: MutableList<Fridge> = mutableListOf()
)