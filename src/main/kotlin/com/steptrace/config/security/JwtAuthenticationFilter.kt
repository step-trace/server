package com.steptrace.config.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

class JwtAuthenticationFilter(
        authenticationManager: AuthenticationManager,
        private val customUserDetailsService: CustomUserDetailsService,
        private val jwtTokenProvider: JwtTokenProvider
) : BasicAuthenticationFilter(authenticationManager) {

    companion object {
        const val AUTH_HEADER = "Authorization"
        const val BEARER = "Bearer "

        private val log = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)
    }

    override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            chain: FilterChain
    ) {
        try {
            val token = resolveToken(request)

            if (token != null) {
                val authentication = getAuthentication(token)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {
            log.error("JWT 인증 처리 중 오류 발생: ", e)
            SecurityContextHolder.clearContext()
        }

        chain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val rawHeader = request.getHeader(AUTH_HEADER)
        return if (rawHeader != null &&
                rawHeader.length > BEARER.length &&
                rawHeader.startsWith(BEARER)) {
            rawHeader.substring(BEARER.length)
        } else {
            null
        }
    }

    private fun getAuthentication(token: String): Authentication {
        val decodedJWT = jwtTokenProvider.verify(token)
        val sub = decodedJWT.subject

        val customUserDetails = customUserDetailsService.loadUserByUsername(sub)

        return UsernamePasswordAuthenticationToken(
                customUserDetails,
                customUserDetails.password,
                customUserDetails.authorities
        )
    }
}