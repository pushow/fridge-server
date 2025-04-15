package com.fridge.fridge_server.domain.food

import com.fridge.fridge_server.domain.fridge.Fridge
import jakarta.persistence.*
import java.time.LocalDate

enum class StorageType {
    COLD,         // 냉장
    FROZEN        // 냉동
}

@Entity
@Table(name = "food")
data class Food(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String,
    val expiryDate: LocalDate,
    val count: Long,
    val memo: String? = null,

    @Enumerated(EnumType.STRING)  // enum 값을 문자열로 저장
    val storageType: StorageType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fridge_id")
    val fridge: Fridge
)