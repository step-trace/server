package com.steptrace.steptrace.support.token.kakao

import com.steptrace.steptrace.auth.OidcPublicKeysResponse
import com.steptrace.steptrace.auth.dto.kakao.KakaoTokenDto
import com.steptrace.steptrace.auth.dto.kakao.KakaoUserInfoDto
import org.springframework.cache.annotation.Cacheable
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
        name = "KakaoOauthClient",
        url = "\${feign.client.kakao.base-url}"
)
interface KakaoOauthClient {
    @PostMapping("\${feign.client.kakao.token-uri}")
    fun getKakaoToken(
            @RequestParam("client_id") restApiKey: String,
            @RequestParam("redirect_uri") redirectUrl: String,
            @RequestParam("code") code: String,
            @RequestParam("grant_type") grantType: String
    ): KakaoTokenDto

    @Cacheable(cacheNames = ["KakaoOICD"], cacheManager = "oidcCacheManager")
    @GetMapping("\${feign.client.kakao.oicd-open-key-uri}")
    fun getKakaoOIDCOpenKeys(): OidcPublicKeysResponse
}

@FeignClient(
        name = "KakaoInfoClient",
        url = "\${feign.client.kakao.oicd-base-url}"
)
interface KakaoInfoClient {
    @GetMapping("\${feign.client.kakao.oicd-userinfo-uri}")
    fun kakaoUserInfo(@RequestHeader("Authorization") accessToken: String): KakaoUserInfoDto
}