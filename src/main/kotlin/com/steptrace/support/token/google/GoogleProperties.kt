package com.steptrace.support.token.google

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "feign.client.google")
data class GoogleProperties(
    var name: String,
    var authUrl: String,
    var tokenUrl: String,
    var userinfoUrl: String,
    var clientId: String,
    var clientSecret: String,
    var redirectUri: String,
    var grantType: String,
    var iss: String,
    var oicdOpenKeyUrl: String,
)