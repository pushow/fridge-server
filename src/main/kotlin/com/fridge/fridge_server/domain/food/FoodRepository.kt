package com.fridge.fridge_server.domain.food

import com.fridge.fridge_server.domain.fridge.Fridge
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface FoodRepository : JpaRepository<Food, Long>{
    // 특정 냉장고에 속한 음식들
    fun findAllByFridge(fridge: Fridge): List<Food>

    // 유통기한이 특정 날짜 이전인 음식 (유통기한 임박용)
    fun findAllByExpiryDateBefore(date: LocalDate): List<Food>

    // 냉장고 + 유통기한 조건
    fun findAllByFridgeAndExpiryDateBefore(fridge: Fridge, date: LocalDate): List<Food>

    fun findAllByFridgeOrderByExpiryDateAsc(fridge: Fridge): List<Food>
}