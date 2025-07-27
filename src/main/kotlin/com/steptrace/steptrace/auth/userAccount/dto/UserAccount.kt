package com.steptrace.steptrace.auth.userAccount.dto

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

@Entity
@Table(name = "user_account")
data class UserAccount(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_account_id")
        val id: Long? = null,

        @Column(name = "profile_nickname", nullable = false)
        val profileNickname: String,

        @Column(name = "account_email", nullable = true, length = 100)
        val accountEmail: String?,

        @Column(nullable = false)
        val sub: String,

        @Column
        val memo: String?,

        @Column(nullable = false, updatable = false)
        val createdAt: LocalDateTime,

        @Column(nullable = false, length = 100, updatable = false)
        val createdBy: String,

        @Column(nullable = false)
        val updatedAt: LocalDateTime?,

        @Column(nullable = false, length = 100)
        val updatedBy: String?
) {
    companion object {
        fun from(dto: UserAccountDto): UserAccount {
            return with(dto) {
                UserAccount(
                    id = id,
                    createdAt = createdAt,
                    createdBy = createdBy ?: "system", // Default to "system" if createdBy is null
                    updatedAt = updatedAt,
                    updatedBy = updatedBy ?: "system", // Default to "system" if updatedBy is null
                    profileNickname = profileNickname,
                    accountEmail = accountEmail,
                    sub = sub,
                    memo = memo
                )
            }
        }
    }
}
