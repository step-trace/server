package com.steptrace.steptrace.config.security

import com.steptrace.steptrace.auth.userAccount.dto.UserAccount
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
        private val userAccount: UserAccount
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return setOf(SimpleGrantedAuthority("ROLE_USER"))
    }

    override fun getPassword(): String {
        return userAccount.sub
    }

    override fun getUsername(): String {
        return userAccount.profileNickname
    }

    override fun isAccountNonExpired(): Boolean {
        return false
    }

    override fun isAccountNonLocked(): Boolean {
        return false
    }

    override fun isCredentialsNonExpired(): Boolean {
        return false
    }

    override fun isEnabled(): Boolean {
        return false
    }

    fun getId(): Long? {
        return userAccount?.id
    }
}

