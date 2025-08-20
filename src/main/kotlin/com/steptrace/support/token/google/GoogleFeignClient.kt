package com.steptrace.support.token.google

import com.steptrace.auth.OidcPublicKeysResponse
import com.steptrace.auth.dto.google.GoogleUserInfoDto
import org.springframework.cache.annotation.Cacheable
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

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