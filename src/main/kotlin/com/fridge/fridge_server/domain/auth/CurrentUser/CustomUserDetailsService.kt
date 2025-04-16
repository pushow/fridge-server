package com.fridge.fridge_server.domain.auth.CurrentUser


import com.fridge.fridge_server.domain.auth.UserPrincipal
import com.fridge.fridge_server.domain.user.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(userId: String): UserDetails {
        val user = userRepository.findById(userId.toLong())
            .orElseThrow { UsernameNotFoundException("사용자를 찾을 수 없습니다.") }
        return UserPrincipal(user)
    }
}