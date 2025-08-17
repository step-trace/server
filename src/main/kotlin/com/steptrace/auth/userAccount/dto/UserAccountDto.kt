package com.steptrace.auth.userAccount.dto

import com.steptrace.auth.dto.UserInfoDto

data class UserAccountDto(
        val profileNickname: String?,
        val name: String?,
        val picture: String,
        val accountEmail: String,
        val sub: String,
        val memo: String? = null
) {
    companion object {
        fun from(dto: UserInfoDto) = with(dto) {
            UserAccountDto(
                    profileNickname = nickname,
                    name = name,
                    picture = picture,
                    accountEmail = email,
                    sub = sub
            )
        }
    }

    fun toEntity(): UserAccountEntity {
        return UserAccountEntity.from(this)
    }
}
