package com.fridge.fridge_server.domain.food

import com.fridge.fridge_server.domain.fridge.Fridge
import jakarta.persistence.*
import java.time.LocalDateTime

enum class StorageType {
    COLD,         // 냉장
    FROZEN        // 냉동
}

@Entity
@Table(name = "food")
class Food(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var name: String,
    var expiryDate: LocalDateTime,
    var count: Long,
    var memo: String? = null,

    var icon: Int = 0,
    @Enumerated(EnumType.STRING)  // enum 값을 문자열로 저장
    var storageType: StorageType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fridge_id")
    val fridge: Fridge
)