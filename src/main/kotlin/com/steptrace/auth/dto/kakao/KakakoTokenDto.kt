package com.steptrace.auth.dto.kakao

@Deprecated("테스트 후 삭제 예정")
data class KakaoTokenDto(
        val tokenType: String,
        val accessToken: String,
        val idToken: String,
        val expiresIn: Long,
        val refreshToken: String,
        val refreshTokenExpiresIn: Long
)