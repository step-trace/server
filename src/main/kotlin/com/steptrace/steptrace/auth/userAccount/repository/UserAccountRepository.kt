package com.steptrace.steptrace.auth.userAccount.repository

import com.steptrace.steptrace.auth.userAccount.dto.UserAccount
import org.springframework.data.jpa.repository.JpaRepository

interface UserAccountRepository : JpaRepository<UserAccount, Long> {
    fun findBySub(sub: String): UserAccount?
}