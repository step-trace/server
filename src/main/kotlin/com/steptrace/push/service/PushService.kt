package com.steptrace.push.service

import com.google.auth.oauth2.GoogleCredentials
import com.steptrace.push.dto.FcmDto
import com.steptrace.support.fcm.FcmFeignClient
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

/**
 * Firebase Cloud Messaging (FCM) 푸시 알림 서비스
 * 
 * Google Firebase Admin SDK를 사용하여 모바일 앱에 푸시 알림을 전송합니다.
 * 사용자가 위험한 맨홀 근처에 있을 때 안전 경고 알림을 보냅니다.
 */
@Service
class PushService(
        private val fcmFeignClient: FcmFeignClient  // FCM API 호출을 위한 Feign 클라이언트
) {

    /**
     * FCM 푸시 메시지를 전송합니다.
     * 
     * Google OAuth 2.0 액세스 토큰을 생성하여 FCM API에 인증한 후
     * 지정된 FCM 토큰으로 푸시 알림을 전송합니다.
     * 
     * @param fcmDto 푸시 메시지 내용 및 대상 토큰
     */
    fun pushFcm(fcmDto: FcmDto) {
        fcmFeignClient.pushMessage("Bearer ${getAccessToken()}", fcmDto)
    }

    /**
     * Firebase Admin SDK를 위한 Google OAuth 2.0 액세스 토큰을 발급받습니다.
     * 
     * 클래스패스에 저장된 Firebase 서비스 계정 키를 사용하여
     * FCM 메시지 전송을 위한 인증 토큰을 생성합니다.
     * 토큰이 만료된 경우 자동으로 갱신됩니다.
     * 
     * @return FCM API 사용을 위한 Bearer 토큰 문자열
     */
    private fun getAccessToken(): String {
        val googleCredentials = GoogleCredentials
                .fromStream(ClassPathResource("firebase/steptrace-a734a-firebase-adminsdk-fbsvc-08656e4cfd.json").inputStream)  // Firebase 서비스 계정 키 로드
                .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))  // FCM 권한 설정

        googleCredentials.refreshIfExpired()  // 만료된 경우 토큰 갱신
        return googleCredentials.accessToken.tokenValue
    }
}