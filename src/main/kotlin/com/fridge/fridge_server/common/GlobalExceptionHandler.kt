package com.fridge.fridge_server.common

import com.fridge.fridge_server.common.CustomException
import io.swagger.v3.oas.annotations.Hidden
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@Hidden
@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException): ResponseEntity<Map<String, Any>> {
        val error = e.errorCode
        return ResponseEntity.status(error.status).body(
            mapOf(
                "code" to error.code,
                "message" to error.message,
                "status" to error.status.value()
            )
        )
    }

    // Optional: IllegalArgumentException 등도 커버
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(e: IllegalArgumentException): ResponseEntity<Map<String, Any>> =
        ResponseEntity.badRequest().body(
            mapOf(
                "code" to "INVALID_ARGUMENT",
                "message" to (e.message ?: "잘못된 요청입니다."),
                "status" to 400
            )
        )
} // end
