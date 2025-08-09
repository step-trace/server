package com.steptrace.auth.dto

data class TokenDto(
        val tokenType: String,
        val accessToken: String,
        val idToken: String
)