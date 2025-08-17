package com.steptrace.auth.dto.google

import com.steptrace.auth.dto.UserInfoDto

data class GoogleUserInfoDto(
        override val sub: String,
        override val name: String,
        override val picture: String,
        override val nickname: String?,
        override val email: String,
        override val emailVerified: Boolean?,
        override val gender: String?,
        override val birthdate: String?,
        override val phoneNumber: String?,
        override val phoneNumberVerified: Boolean?
): UserInfoDto
