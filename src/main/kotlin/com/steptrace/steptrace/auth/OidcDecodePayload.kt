package com.steptrace.steptrace.auth

data class OidcDecodePayload(
        /** issuer ex https://kauth.kakao.com */
        val iss: String,
        /** client id */
        val aud: String,
        /** oauth provider account unique id */
        val sub: String,
        val email: String?
) {
    companion object {
        fun of(iss: String, aud: String, sub: String, email: String): OidcDecodePayload {
            return OidcDecodePayload(iss, aud, sub, email)
        }
    }
}