package com.fridge.fridge_server.common

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus

enum class ErrorCode(
    val code: String,
    val message: String,

    @field:Schema(hidden = true)
    val status: HttpStatus
) {
    // USER
    USER_NOT_FOUND("USR001", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_EXISTS("USR002", "이미 사용 중인 이메일입니다.", HttpStatus.BAD_REQUEST),

    // FRIDGE
    FRIDGE_NOT_FOUND("FRD001", "냉장고를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // FOOD
    FOOD_NOT_FOUND("FOD001", "음식을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // FAMILY
    FAMILY_NOT_FOUND("FAM001", "가족 그룹을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // FAMILY_INVITE
    INVITE_ALREADY_EXISTS("INV001", "이미 초대가 존재합니다.", HttpStatus.CONFLICT),
    INVITE_NOT_FOUND("INV002", "해당 초대를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // AUTH
    LOGIN_FAILED("AUTH001", "이메일 또는 비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_INVALID("AUTH002", "유효하지 않은 리프레시 토큰입니다.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_EXPIRED("AUTH003", "리프레시 토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED)
}