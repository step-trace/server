package com.steptrace.support.token

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class TokenProperties(
        var secret: String,
        var issuer: String,
        var expiration: String,
        var prefix: String,
)
