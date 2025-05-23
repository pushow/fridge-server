package com.fridge.fridge_server.domain.user

import com.fridge.fridge_server.domain.family.FamilyGroup
import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var name: String,
    val email: String,
    var password: String, // hashed

    var profile: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_group_id")
    var familyGroup: FamilyGroup,  // 가족 그룹 참조
){
    fun changeFamilyGroup(newFamilyGroup: FamilyGroup) {
        this.familyGroup = newFamilyGroup
    }
}