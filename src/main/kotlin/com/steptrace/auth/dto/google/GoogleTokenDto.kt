package com.steptrace.auth.dto.google

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@Deprecated("테스트 후 삭제 예정")
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy::class)
data class GoogleTokenDto(
        val accessToken: String,
        val expiresIn: Long,
        val tokenType: String,
        val idToken: String,
        val scope: String
)
