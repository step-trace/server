package com.steptrace.support.fcm

import com.steptrace.push.dto.FcmDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
        name = "FcmPushClient",
        url = "https://fcm.googleapis.com"
)
interface FcmFeignClient {
    @PostMapping("/v1/projects/steptrace-a734a/messages:send")
    fun pushMessage(
            @RequestHeader("Authorization") accessToken: String,
            @RequestBody fcmDto: FcmDto
    ): String
}