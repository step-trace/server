package com.steptrace.auth.controller

import com.steptrace.auth.dto.TokenResponse
import com.steptrace.auth.dto.google.GoogleTokenDto
import com.steptrace.auth.dto.kakao.KakaoTokenDto
import com.steptrace.auth.service.AuthService
import com.steptrace.config.security.CustomUserDetails
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
        private val authService: AuthService,
) {
    @PostMapping("/v1/kakao")
    fun kakaoLogin(
            @RequestBody kakaoTokenDto: KakaoTokenDto
    ): TokenResponse {
        return authService.isKakaoUserRegistered(kakaoTokenDto)
    }

    @PostMapping("/v1/google")
    fun googleLogin(
            @RequestBody googleTokenDto: GoogleTokenDto
    ): TokenResponse {
        return authService.isGoogleUserRegistered(googleTokenDto)
    }

    @DeleteMapping("/users")
    @ResponseBody
    fun userAccount(@AuthenticationPrincipal userAccount: CustomUserDetails) {
        authService.deleteUserAccount(userAccount.password)
    }
}