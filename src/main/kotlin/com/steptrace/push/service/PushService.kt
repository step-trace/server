package com.steptrace.push.service

import com.google.auth.oauth2.GoogleCredentials
import com.steptrace.push.dto.FcmDto
import com.steptrace.support.fcm.FcmFeignClient
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

@Service
class PushService(
        private val fcmFeignClient: FcmFeignClient
) {

    fun pushFcm(fcmDto: FcmDto) {
        fcmFeignClient.pushMessage("Bearer ${getAccessToken()}", fcmDto)
    }

    private fun getAccessToken(): String {
        val googleCredentials = GoogleCredentials
                .fromStream(ClassPathResource("firebase/steptrace-a734a-firebase-adminsdk-fbsvc-08656e4cfd.json").inputStream)
                .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))

        googleCredentials.refreshIfExpired()
        return googleCredentials.accessToken.tokenValue
    }
}