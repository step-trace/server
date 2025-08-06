package com.steptrace.config.security

import org.springframework.security.core.userdetails.UserDetailsService
import com.steptrace.auth.userAccount.repository.UserAccountRepository
import com.steptrace.exception.UserNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
        private val userAccountRepository: UserAccountRepository
) : UserDetailsService {

    override fun loadUserByUsername(sub: String): CustomUserDetails {
        val user = userAccountRepository.findBySub(sub)
                ?: throw UserNotFoundException()
        return CustomUserDetails(user)
    }
}