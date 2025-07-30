package com.steptrace.steptrace.auth.userAccount.dto

import com.steptrace.steptrace.auth.userAccount.BaseAuditEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.GenerationType
import jakarta.persistence.Column

@Entity
@Table(name = "user_account")
data class UserAccountEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_account_id")
        val id: Long? = null,

        @Column(name = "profile_nickname")
        val profileNickname: String? = null,

        @Column(name = "name")
        val name: String? = null,

        @Column(name = "account_email")
        val accountEmail: String? = null,

        @Column(name = "sub", nullable = false)
        val sub: String,

        @Column(name = "memo")
        val memo: String? = null,
) : BaseAuditEntity() {
    companion object {
        fun from(dto: UserAccountDto): UserAccountEntity {
            return with(dto) {
                UserAccountEntity(
                        profileNickname = profileNickname,
                        name = name,
                        accountEmail = accountEmail,
                        sub = sub,
                        memo = memo
                )
            }
        }
    }
}
