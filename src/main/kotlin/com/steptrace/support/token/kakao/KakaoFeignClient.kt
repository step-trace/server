package com.steptrace.support.token.kakao

import com.steptrace.auth.OidcPublicKeysResponse
import com.steptrace.auth.dto.kakao.KakaoUserInfoDto
import org.springframework.cache.annotation.Cacheable
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
        name = "KakaoOauthClient",
        url = "\${feign.client.kakao.base-url}"
)
interface KakaoOauthClient {
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