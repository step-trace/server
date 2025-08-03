package com.steptrace.auth.controller

import com.steptrace.auth.dto.google.GoogleTokenDto
import com.steptrace.auth.dto.kakao.KakaoTokenDto
import com.steptrace.auth.service.AuthService
import com.steptrace.config.security.CustomUserDetails
import com.steptrace.support.token.google.GoogleProperties
import com.steptrace.support.token.kakao.KakaoProperties
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/auth")
class AuthController(
        private val authService: AuthService,
        private val kakaoProperties: KakaoProperties,
        private val googleProperties: GoogleProperties
) {

    @GetMapping("/kakao")
    fun kakaoLogin(): String {
        return "redirect:" + kakaoProperties.authUrl
    }

    @GetMapping("/kakao/callback")
    @ResponseBody
    fun getKakaoToken(@RequestParam code: String): KakaoTokenDto {
        val kakaoTokenDto = authService.getKakaoToken(code)

        authService.isKakaoUserRegistered(kakaoTokenDto)

        return kakaoTokenDto
    }

    @GetMapping("/google")
    fun googleLogin(): String {
        return "redirect:${googleProperties.authUrl}"
    }

    @GetMapping("/google/callback")
    @ResponseBody
    fun getGoogleToken(@RequestParam code: String): GoogleTokenDto {
        val googleTokenDto = authService.getGoogleToken(code)

        authService.isGoogleUserRegistered(googleTokenDto)

        return googleTokenDto
    }

    @DeleteMapping("/users")
    @ResponseBody
    fun userAccount(@AuthenticationPrincipal userAccount: CustomUserDetails) {
        authService.deleteUserAccount(userAccount.password)
    }
}