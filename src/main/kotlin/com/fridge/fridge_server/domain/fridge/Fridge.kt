package com.fridge.fridge_server.domain.fridge

import com.fridge.fridge_server.domain.family.FamilyGroup
import com.fridge.fridge_server.domain.food.Food
import jakarta.persistence.*

@Entity
@Table(name = "fridge")
class Fridge(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var name: String,

    @ManyToOne
    @JoinColumn(name = "family_group_id")
    val familyGroup: FamilyGroup,

    @OneToMany(mappedBy = "fridge", cascade = [CascadeType.ALL], orphanRemoval = true)
    val foods: List<Food> = mutableListOf()
)