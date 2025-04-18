package com.fridge.fridge_server.domain.auth

import com.fridge.fridge_server.domain.auth.Refresh.RefreshToken
import com.fridge.fridge_server.domain.auth.Refresh.RefreshTokenRepository
import com.fridge.fridge_server.domain.auth.dto.LoginRequest
import com.fridge.fridge_server.domain.auth.dto.TokenReissueRequest
import com.fridge.fridge_server.domain.auth.dto.TokenResponse
import com.fridge.fridge_server.domain.user.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

import com.fridge.fridge_server.common.CustomException
import com.fridge.fridge_server.common.ErrorCode


@Service
class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder
) {

    fun login(request: LoginRequest): TokenResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw CustomException(ErrorCode.LOGIN_FAILED)

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw CustomException(ErrorCode.LOGIN_FAILED)
        }

        val accessToken = jwtTokenProvider.createAccessToken(user.id)
        val refreshToken = jwtTokenProvider.createRefreshToken(user.id)

        val expiry = LocalDateTime.now().plusSeconds(jwtTokenProvider.refreshExpSeconds())
        val saved = refreshTokenRepository.findById(user.id)

        if (saved.isPresent) {
            val token = saved.get()
            token.token = refreshToken
            token.expiryDate = expiry
        } else {
            refreshTokenRepository.save(
                RefreshToken(userId = user.id, token = refreshToken, expiryDate = expiry)
            )
        }

        return TokenResponse(accessToken = accessToken, refreshToken = refreshToken)
    }

    fun reissue(request: TokenReissueRequest): TokenResponse {
        val stored = refreshTokenRepository.findByToken(request.refreshToken)
            ?: throw CustomException(ErrorCode.REFRESH_TOKEN_INVALID)

        if (stored.expiryDate.isBefore(LocalDateTime.now())) {
            throw CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED)
        }

        val newAccessToken = jwtTokenProvider.createAccessToken(stored.userId)
        val newRefreshToken = jwtTokenProvider.createRefreshToken(stored.userId)
        stored.token = newRefreshToken
        stored.expiryDate = LocalDateTime.now().plusSeconds(jwtTokenProvider.refreshExpSeconds())

        return TokenResponse(accessToken = newAccessToken, refreshToken = newRefreshToken)
    }

    fun logout(userId: Long) {
        refreshTokenRepository.deleteById(userId)
    }
}