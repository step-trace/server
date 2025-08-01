package com.steptrace.steptrace.auth.userAccount.repository

import com.steptrace.steptrace.auth.userAccount.dto.UserAccountEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserAccountRepository : JpaRepository<UserAccountEntity, Long> {
    fun findBySub(sub: String): UserAccountEntity?
    fun existsBySub(sub: String): Boolean
    fun deleteBySub(sub: String)
}