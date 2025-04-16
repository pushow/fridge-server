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

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder
) {

    fun login(request: LoginRequest): TokenResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw IllegalArgumentException("존재하지 않는 이메일입니다.")

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("비밀번호가 일치하지 않습니다.")
        }

        val accessToken = jwtTokenProvider.createAccessToken(user.id!!)
        val refreshToken = jwtTokenProvider.createRefreshToken(user.id)

        // 기존 토큰이 있으면 갱신, 없으면 새로 저장
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
            ?: throw IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.")

        if (stored.expiryDate.isBefore(LocalDateTime.now())) {
            throw IllegalArgumentException("리프레시 토큰이 만료되었습니다.")
        }

        val newAccessToken = jwtTokenProvider.createAccessToken(stored.userId)
        val newRefreshToken = jwtTokenProvider.createRefreshToken(stored.userId)
        stored.token = newRefreshToken
        stored.expiryDate = LocalDateTime.now().plusSeconds(jwtTokenProvider.refreshExpSeconds())

        return TokenResponse(accessToken = newAccessToken, refreshToken = newRefreshToken)
    }
}