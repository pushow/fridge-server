package com.fridge.fridge_server.domain.auth.Refresh

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class RefreshToken(
    @Id
    val userId: Long,

    @Column(nullable = false)
    var token: String,

    @Column(nullable = false)
    var expiryDate: LocalDateTime
)