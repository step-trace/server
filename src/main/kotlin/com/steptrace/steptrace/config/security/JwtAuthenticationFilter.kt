package com.steptrace.steptrace.config.security

import com.steptrace.steptrace.support.token.OauthOidcHelper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

class JwtAuthenticationFilter(
        authenticationManager: AuthenticationManager,
        private val oauthOidcHelper: OauthOidcHelper,
        private val customUserDetailsService: CustomUserDetailsService
) : BasicAuthenticationFilter(authenticationManager) {

    companion object {
        const val AUTH_HEADER = "Authorization"
        const val BEARER = "Bearer "

        val log = org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)
    }

    override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            chain: FilterChain
    ) {
        val token = resolveToken(request)

        log.info("JWT Token: $token")

        if (token != null) {
            val authentication = getAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication
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
        val oidcDecodePayload = oauthOidcHelper.getKakaoOIDCDecodePayload(token)
        val customUserDetails = customUserDetailsService.loadUserByUsername(oidcDecodePayload.sub)
        return UsernamePasswordAuthenticationToken(
                customUserDetails,
                customUserDetails.password,
                customUserDetails.authorities
        )
    }
}