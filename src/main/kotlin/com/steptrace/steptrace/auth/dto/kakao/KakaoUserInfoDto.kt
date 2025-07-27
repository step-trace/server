package com.steptrace.steptrace.auth.dto.kakao

data class KakaoUserInfoDto(
        val sub: String,
        val name: String?,
        val nickname: String,
        val profileImage: String?,
        val email: String?,
        val emailVerified: Boolean?,
        val gender: String?,
        val birthdate: String?,
        val phoneNumber: String?,
        val phoneNumberVerified: Boolean?
)