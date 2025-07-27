package com.steptrace.steptrace.config.security

import org.springframework.security.core.userdetails.UserDetailsService
import com.steptrace.steptrace.auth.userAccount.repository.UserAccountRepository
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
        private val userAccountRepository: UserAccountRepository
) : UserDetailsService {

    override fun loadUserByUsername(sub: String): CustomUserDetails {
        val user = userAccountRepository.findBySub(sub)
                ?: throw IllegalArgumentException() //todo: 커스텀 예외 처리 필요
        return CustomUserDetails(user)
    }
}