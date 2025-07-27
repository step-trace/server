package com.steptrace.steptrace.auth.userAccount.dto

import java.io.Serializable
import java.time.LocalDateTime

data class UserAccountDto(
        val id: Long?,
        val createdAt: LocalDateTime,
        val createdBy: String?,
        val updatedAt: LocalDateTime?,
        val updatedBy: String?,
        val profileNickname: String,
        val accountEmail: String?,
        val sub: String,
        val memo: String?
        ) : Serializable {
            companion object {
            fun of(profileNickname: String, sub: String): UserAccountDto {
                return UserAccountDto(
                        id = null,
                        createdAt = LocalDateTime.now(),
                        createdBy = null,
                        updatedAt = LocalDateTime.now(),
                        updatedBy = null,
                        profileNickname = profileNickname,
                        accountEmail = null,
                        sub = sub,
                        memo = null
                )
            }
        }

            fun toEntity(): UserAccount {
        return UserAccount.from(this)
    }
}
