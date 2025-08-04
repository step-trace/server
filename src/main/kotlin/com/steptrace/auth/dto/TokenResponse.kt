package com.steptrace.auth.dto

data class TokenResponse(
        val jwtToken: String
) {
    companion object {
        fun from(token: String): TokenResponse {
            return TokenResponse(jwtToken = token)
        }
    }
}
