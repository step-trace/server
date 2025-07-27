package com.steptrace.steptrace.support.token.kakao

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "feign.client.kakao")
data class KakaoProperties(
        var name: String,
        var baseUrl: String,
        var authUrl: String,
        var tokenUri: String,
        var oicdOpenKeyUri: String,
        var restApiKey: String,
        var redirectUri: String,
        var grantType: String
)