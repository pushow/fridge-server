package com.fridge.fridge_server.domain.auth

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") secretKey: String,
    @Value("\${jwt.access-exp}") private val accessExp: Long,
    @Value("\${jwt.refresh-exp}") private val refreshExp: Long
) {
    private val key = Keys.hmacShaKeyFor(secretKey.toByteArray())

    fun createAccessToken(userId: Long): String = createToken(userId, accessExp)
    fun createRefreshToken(userId: Long): String = createToken(userId, refreshExp)

    private fun createToken(userId: Long, exp: Long): String {
        val now = Date()
        val expiry = Date(now.time + exp)
        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getUserId(token: String): Long =
        Jwts.parserBuilder().setSigningKey(key).build()
            .parseClaimsJws(token).body.subject.toLong()

    fun isValid(token: String): Boolean = try {
        val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
        !claims.body.expiration.before(Date())
    } catch (e: Exception) {
        false
    }

    fun refreshExpSeconds(): Long = refreshExp / 1000
}