package com.fridge.fridge_server.domain.fridge.dto

import com.fridge.fridge_server.domain.fridge.Fridge

data class FridgeResponse(
    val id: Long,
    val name: String
) {
    companion object {
        fun from(fridge: Fridge): FridgeResponse {
            return FridgeResponse(fridge.id, fridge.name)
        }
    }
}