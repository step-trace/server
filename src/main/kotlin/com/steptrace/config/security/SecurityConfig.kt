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

@Configuration
class SecurityConfig(
        private val customUserDetailsService: CustomUserDetailsService,
        private val jwtTokenProvider: JwtTokenProvider
) {

    inner class CustomSecurityFilterManager : AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity>() {
        override fun configure(builder: HttpSecurity) {
            val authenticationManager = builder.getSharedObject(AuthenticationManager::class.java)
            builder.addFilter(JwtAuthenticationFilter(authenticationManager, customUserDetailsService, jwtTokenProvider))
            super.configure(builder)
        }
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOriginPatterns = listOf("*")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true
        configuration.exposedHeaders = listOf("Authorization", "Content-Type")
        
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
                .cors { it.configurationSource(corsConfigurationSource()) }
                .httpBasic { it.disable() }
                .formLogin { it.disable() }
                .rememberMe { it.disable() }
                .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
                .with(CustomSecurityFilterManager(), Customizer.withDefaults())
                .headers { headers -> headers.frameOptions { it.sameOrigin() } }

        http.authorizeHttpRequests {
            it.requestMatchers(HttpMethod.DELETE, "/api/auth/users").authenticated()
                    .requestMatchers("/auth/**").permitAll() // 테스트 후 삭제 예정
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/error/**").permitAll()
                    .anyRequest().authenticated()
        }

        return http.build()
    }
}