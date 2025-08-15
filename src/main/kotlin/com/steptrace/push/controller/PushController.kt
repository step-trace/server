package com.steptrace.push.controller

import com.steptrace.push.dto.FcmDto
import com.steptrace.push.dto.FcmRequest
import com.steptrace.push.service.PushService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/push")
class PushController(
        private val pushService: PushService
) {

    @PostMapping("/v1/fcm")
    fun pushFcm(
            @RequestBody fcmRequest: FcmRequest
    ) {
        pushService.pushFcm(FcmDto.from(fcmRequest))
    }
}