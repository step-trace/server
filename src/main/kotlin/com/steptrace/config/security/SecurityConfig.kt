package com.steptrace.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

/**
 * Spring Security 보안 설정 클래스
 * 
 * JWT 인증 방식을 사용하는 Stateless 보안 설정을 구성합니다.
 * CORS 설정, 인증이 필요한/불필요한 엔드포인트를 정의하고,
 * 커스텀 JWT 인증 필터를 등록합니다.
 */
@Configuration
class SecurityConfig(
        private val customUserDetailsService: CustomUserDetailsService,  // 커스텀 사용자 세부 정보 서비스
        private val jwtTokenProvider: JwtTokenProvider                   // JWT 토큰 생성 및 검증 제공자
) {

    /**
     * 커스텀 보안 필터 관리자
     * 
     * JWT 인증 필터를 Spring Security 필터 체인에 추가합니다.
     * 모든 HTTP 요청에 대해 JWT 토큰을 검증하고 인증을 처리합니다.
     */
    inner class CustomSecurityFilterManager : AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity>() {
        override fun configure(builder: HttpSecurity) {
            val authenticationManager = builder.getSharedObject(AuthenticationManager::class.java)
            // JWT 인증 필터를 필터 체인에 추가
            builder.addFilter(JwtAuthenticationFilter(authenticationManager, customUserDetailsService, jwtTokenProvider))
            super.configure(builder)
        }
    }

    /**
     * CORS (Cross-Origin Resource Sharing) 설정
     * 
     * 모든 도메인에서의 요청을 허용하도록 설정합니다.
     * 모바일 앱 및 다양한 프론트엔드에서 API에 접근할 수 있도록 하기 위해
     * 비교적 열린 CORS 정책을 적용합니다.
     * 
     * @return CORS 설정 소스
     */
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOriginPatterns = listOf("*")                          // 모든 도메인에서의 요청 허용
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")     // 허용될 HTTP 메서드
        configuration.allowedHeaders = listOf("*")                                // 모든 헤더 허용
        configuration.allowCredentials = true                                     // 인증 정보 포함 요청 허용
        configuration.exposedHeaders = listOf("Authorization", "Content-Type")   // 클라이언트에게 노출될 헤더
        
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)  // 모든 경로에 CORS 설정 적용
        return source
    }

    /**
     * 메인 보안 필터 체인 설정
     * 
     * Spring Security의 핵심 보안 설정을 정의합니다.
     * JWT 기반 인증을 사용하도록 설정하고, 엔드포인트별 인증 요구사항을 정의합니다.
     * 
     * @param http HttpSecurity 빌더
     * @return 설정된 SecurityFilterChain
     */
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }                                                           // CSRF 보호 비활성화 (JWT 사용으로 불필요)
                .cors { it.configurationSource(corsConfigurationSource()) }                 // CORS 설정 적용
                .httpBasic { it.disable() }                                                  // HTTP Basic 인증 비활성화
                .formLogin { it.disable() }                                                  // 폼 기반 로그인 비활성화
                .rememberMe { it.disable() }                                                 // Remember Me 기능 비활성화
                .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }  // 세션 사용 안함 (Stateless)
                .with(CustomSecurityFilterManager(), Customizer.withDefaults())             // 커스텀 JWT 필터 추가
                .headers { headers -> headers.frameOptions { it.sameOrigin() } }             // X-Frame-Options: SAMEORIGIN

        // 엔드포인트별 인증 요구사항 정의
        http.authorizeHttpRequests {
            it.requestMatchers("/auth/**").permitAll()                                     // 테스트용 인증 엔드포인트 (테스트 후 삭제 예정)
                    .requestMatchers(HttpMethod.DELETE, "/api/auth/users").authenticated()  // 사용자 삭제는 인증 필요
                    .requestMatchers("/api/auth/**").permitAll()                          // OAuth 로그인 엔드포인트
                    .requestMatchers("/api/v1/manholes").permitAll()                      // 맨홀 조회 엔드포인트
                    .requestMatchers("/api/v1/manholes/*").permitAll()                    // 맨홀 상세 조회 엔드포인트
                    .requestMatchers("/v1/manholes/push/fcm").permitAll()                 // FCM 푸시 엔드포인트
                    .requestMatchers("/api/auth/**").permitAll()                          // 인증 관련 엔드포인트 (중복)
                    .requestMatchers("/error/**").permitAll()                             // 오류 처리 엔드포인트
                    .anyRequest().authenticated()                                          // 나머지 모든 요청은 인증 필수
        }

        return http.build()
    }
}