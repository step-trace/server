package com.steptrace.auth.dto.kakao

import com.steptrace.auth.dto.TokenDto

data class KakaoTokenDto(
        override val tokenType: String,
        override val accessToken: String,
        override val idToken: String,
        override val expiresIn: Long,
        val refreshToken: String,
        val refreshTokenExpiresIn: Long
): TokenDto