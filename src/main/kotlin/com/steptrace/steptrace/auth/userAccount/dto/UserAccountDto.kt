package com.steptrace.steptrace.auth.userAccount.dto

import com.steptrace.steptrace.auth.dto.UserInfoDto

data class UserAccountDto(
        val profileNickname: String?,
        val name: String?,
        val accountEmail: String?,
        val sub: String,
        val memo: String? = null
) {
    companion object {
        fun from(dto: UserInfoDto): UserAccountDto {
            return UserAccountDto(
                    profileNickname = dto.nickname,
                    name = dto.name,
                    accountEmail = dto.email,
                    sub = dto.sub
            )
        }
    }

    fun toEntity(): UserAccountEntity {
        return UserAccountEntity.from(this)
    }
}
