package com.steptrace.auth

data class OidcPublicKeyDto(
        val kid: String = "",
        val kty: String = "",
        val alg: String = "",
        val use: String = "",
        val n: String = "",
        val e: String = ""
)