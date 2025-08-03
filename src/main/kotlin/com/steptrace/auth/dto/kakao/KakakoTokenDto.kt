package com.steptrace.auth.dto.kakao

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.steptrace.auth.dto.TokenDto

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KakaoTokenDto(
        override val tokenType: String,
        override val accessToken: String,
        override val idToken: String,
        override val expiresIn: Long,
        val refreshToken: String,
        val refreshTokenExpiresIn: Long
): TokenDto