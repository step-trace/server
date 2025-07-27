package com.steptrace.steptrace.auth.controller

import com.steptrace.steptrace.auth.dto.kakao.KakaoTokenDto
import com.steptrace.steptrace.auth.service.AuthService
import com.steptrace.steptrace.support.token.kakao.KakaoProperties
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/auth")
class AuthController(
        private val authService: AuthService,
        private val kakaoProperties: KakaoProperties
) {

    @GetMapping("/kakao")
    fun kakaoLogin(): String {
        return "redirect:" + kakaoProperties.authUrl
    }

    @GetMapping("/kakao/callback")
    fun getKakaoToken(@RequestParam code: String): ResponseEntity<KakaoTokenDto> {
        val kakaoTokenDto = authService.getKakaoToken(code)

        authService.isUserRegistered(kakaoTokenDto)

        return ResponseEntity.ok(kakaoTokenDto)
    }
}