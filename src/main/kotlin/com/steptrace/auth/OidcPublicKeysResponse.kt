package com.steptrace.auth

data class OidcPublicKeysResponse(
        val keys: List<OidcPublicKeyDto> = emptyList()
)