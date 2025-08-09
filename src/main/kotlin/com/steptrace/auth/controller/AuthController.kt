package com.steptrace.auth.controller

import com.steptrace.auth.dto.TokenDto
import com.steptrace.auth.dto.TokenResponse
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
            @RequestBody tokenDto: TokenDto
    ): TokenResponse {
        return authService.isKakaoUserRegistered(tokenDto)
    }

    @PostMapping("/v1/google")
    fun googleLogin(
            @RequestBody tokenDto: TokenDto
    ): TokenResponse {
        return authService.isGoogleUserRegistered(tokenDto)
    }

    @DeleteMapping("/users")
    @ResponseBody
    fun userAccount(@AuthenticationPrincipal userAccount: CustomUserDetails) {
        authService.deleteUserAccount(userAccount.password)
    }
}