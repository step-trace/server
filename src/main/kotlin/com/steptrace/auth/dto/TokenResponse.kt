package com.steptrace.auth.dto

data class TokenResponse(
        val jwt: String
) {
    companion object {
        fun from(token: String): TokenResponse {
            return TokenResponse(jwt = token)
        }
    }
}
