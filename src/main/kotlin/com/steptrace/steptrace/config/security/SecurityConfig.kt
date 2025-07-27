package com.steptrace.steptrace.config.security

import com.steptrace.steptrace.support.token.OauthOidcHelper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig(
        private val oauthOidcHelper: OauthOidcHelper,
        private val customUserDetailsService: CustomUserDetailsService
) {

    inner class CustomSecurityFilterManager : AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity>() {
        override fun configure(builder: HttpSecurity) {
            val authenticationManager = builder.getSharedObject(AuthenticationManager::class.java)
            builder.addFilter(JwtAuthenticationFilter(authenticationManager, oauthOidcHelper, customUserDetailsService))
            super.configure(builder)
        }
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val swaggerPermitUrls = arrayOf(
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html"
        )

        http.csrf { it.disable() }
                .cors { it.disable() }
                .httpBasic { it.disable() }
                .formLogin { it.disable() }
                .rememberMe { it.disable() }
                .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
                .with(CustomSecurityFilterManager(), Customizer.withDefaults())
                .headers { headers -> headers.frameOptions { it.sameOrigin() } }

        // todo: api 수정 필요
        http.authorizeHttpRequests {
            it.requestMatchers("/library/reservation/**").hasRole("USER")
                    .requestMatchers("/h2-console/**").permitAll()
                    .requestMatchers(*swaggerPermitUrls).permitAll()
                    .requestMatchers("/library/seats").permitAll()
                    .requestMatchers("/**").hasRole("USER")
        }

        return http.build()
    }
}