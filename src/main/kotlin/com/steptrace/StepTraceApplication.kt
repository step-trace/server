package com.steptrace

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * StepTrace 애플리케이션의 메인 클래스
 * 
 * 위험 맨홀 추적 및 관리 시스템으로, 다음 기능들을 제공:
 * - OAuth 2.0 인증 (Google, Kakao)
 * - AI 기반 이미지 분석을 통한 맨홀 상태 평가
 * - 위치 기반 위험 맨홀 알림
 * - 자동화된 일일 리포트 생성
 * - AWS S3를 통한 이미지 저장 관리
 */
@SpringBootApplication
@EnableFeignClients        // 외부 API 연동을 위한 Feign 클라이언트 활성화 (Google, Kakao, FCM, AI)
@EnableJpaAuditing         // JPA 엔티티의 생성일/수정일 자동 관리
@EnableScheduling          // 일일 맨홀 리포트 생성을 위한 스케줄링 활성화
@ConfigurationPropertiesScan  // 설정 클래스들의 자동 스캔
class StepTraceApplication

/**
 * 애플리케이션 시작점
 * 
 * @param args 명령행 인수
 */
fun main(args: Array<String>) {
    runApplication<StepTraceApplication>(*args)
}
