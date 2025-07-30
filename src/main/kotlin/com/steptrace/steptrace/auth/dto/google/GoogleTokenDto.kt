package com.steptrace.steptrace.auth.dto.google

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.steptrace.steptrace.auth.dto.TokenDto

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy::class)
data class GoogleTokenDto(
        override val accessToken: String,
        override val expiresIn: Long,
        override val tokenType: String,
        override val idToken: String,
        val scope: String
): TokenDto
