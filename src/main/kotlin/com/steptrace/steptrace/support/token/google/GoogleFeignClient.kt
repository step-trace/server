package com.steptrace.steptrace.support.token.google

import com.steptrace.steptrace.auth.OidcPublicKeysResponse
import com.steptrace.steptrace.auth.dto.google.GoogleTokenDto
import com.steptrace.steptrace.auth.dto.google.GoogleUserInfoDto
import org.springframework.cache.annotation.Cacheable
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
        name = "GoogleOauthClient",
        url = "\${feign.client.google.token-url}"
)
interface GoogleOauthClient {
    @PostMapping(
            "/token",
            consumes = ["application/x-www-form-urlencoded"])
    fun getGoogleToken(
            @RequestParam("code") code: String,
            @RequestParam("client_id") clientId: String,
            @RequestParam("client_secret") clientSecret: String,
            @RequestParam("redirect_uri") redirectUri: String,
            @RequestParam("grant_type") grantType: String
    ): GoogleTokenDto
}

@FeignClient(
        name = "GoogleOIDCClient",
        url = "\${feign.client.google.oicd-open-key-url}"
)
interface GoogleOIDCClient {
    @Cacheable(cacheNames = ["GoogleOICD"], cacheManager = "oidcCacheManager")
    @GetMapping("/oauth2/v3/certs")
    fun getGoogleOIDCOpenKeys(): OidcPublicKeysResponse
}

@FeignClient(
        name = "GoogleResource",
        url = "\${feign.client.google.userinfo-url}"
)
interface GoogleInfoClient {
    @GetMapping(
            "/oauth2/v3/userinfo",
            consumes = ["application/x-www-form-urlencoded"])
    fun googleUserInfo(@RequestHeader("Authorization") accessToken: String): GoogleUserInfoDto
}