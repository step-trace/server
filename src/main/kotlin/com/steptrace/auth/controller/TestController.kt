package com.steptrace.auth.controller

import com.steptrace.auth.dto.kakao.KakaoTokenDto
import com.steptrace.auth.service.AuthService
import com.steptrace.config.security.CustomUserDetails
import com.steptrace.support.token.kakao.KakaoProperties
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class TestController(
        private val kakaoProperties: KakaoProperties,
        private val authService: AuthService
) {
    @GetMapping("/test")
    @ResponseBody
    fun test(): String {
        return "Hello, this is a test endpoint!"
    }

    @DeleteMapping("/delete")
    @ResponseBody
    fun userAccount(@AuthenticationPrincipal userAccount: CustomUserDetails) {
        logger.info(userAccount.username + " 계정 삭제 요청")
    }

    @GetMapping("/auth/kakao")
    fun kakaoLogin(): String {
        return "redirect:" + kakaoProperties.authUrl
    }

    @GetMapping("/auth/kakao/callback")
    @ResponseBody
    fun getKakaoToken(@RequestParam code: String): KakaoTokenDto {
        return authService.getKakaoToken(code)
    }

    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger(AuthController::class.java)
    }
}