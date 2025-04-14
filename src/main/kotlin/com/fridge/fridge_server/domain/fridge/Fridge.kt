package com.fridge.fridge_server.domain.fridge

import com.fridge.fridge_server.domain.family.FamilyGroup
import jakarta.persistence.*

@Entity
class Fridge(
    @Id @GeneratedValue val id: Long = 0,
    val name: String,

    @ManyToOne
    @JoinColumn(name = "family_group_id")
    val familyGroup: FamilyGroup
)