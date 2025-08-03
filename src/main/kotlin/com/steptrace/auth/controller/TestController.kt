package com.steptrace.auth.controller

import com.steptrace.config.security.CustomUserDetails
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {
    @GetMapping("/test")
    fun test(): String {
        return "Hello, this is a test endpoint!"
    }

    @DeleteMapping("/delete")
    fun userAccount(@AuthenticationPrincipal userAccount: CustomUserDetails) {
        logger.info(userAccount.username + " 계정 삭제 요청")
    }

    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger(AuthController::class.java)
    }
}