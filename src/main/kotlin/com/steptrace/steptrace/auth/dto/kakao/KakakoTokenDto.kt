package com.steptrace.steptrace.auth.dto.kakao

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KakaoTokenDto(
        val tokenType: String,
        val accessToken: String,
        val idToken: String,
        val expiresIn: Long,
        val refreshToken: String,
        val refreshTokenExpiresIn: Long
)