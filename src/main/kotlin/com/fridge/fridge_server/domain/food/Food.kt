package com.fridge.fridge_server.domain.food

import jakarta.persistence.*
import java.time.LocalDate

@Entity
data class Food(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val expiryDate: LocalDate,
    val count: Long,
    val memo: String? = null
)