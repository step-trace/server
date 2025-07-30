package com.steptrace.steptrace.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.steptrace.steptrace.auth.OidcDecodePayload
import com.steptrace.steptrace.support.token.OauthOidcHelper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.util.*

class JwtAuthenticationFilter(
        authenticationManager: AuthenticationManager,
        private val oauthOidcHelper: OauthOidcHelper,
        private val customUserDetailsService: CustomUserDetailsService
) : BasicAuthenticationFilter(authenticationManager) {

    companion object {
        const val AUTH_HEADER = "Authorization"
        const val BEARER = "Bearer "

        private val objectMapper = ObjectMapper()
    }

    override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            chain: FilterChain
    ) {
        val token = resolveToken(request)

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
        val issuer = getTokenIssuer(token)
        val oidcDecodePayload = getOidcDecodePayload(token, issuer)
        val customUserDetails = customUserDetailsService.loadUserByUsername(oidcDecodePayload.sub)

        return UsernamePasswordAuthenticationToken(
                customUserDetails,
                customUserDetails.password,
                customUserDetails.authorities
        )
    }

    private fun getTokenIssuer(token: String): String {
        return try {
            validateTokenFormat(token)

            val unsignedToken = token.split(".")[1]
            val decodedPayload = String(Base64.getUrlDecoder().decode(unsignedToken))

            val payloadMap = objectMapper.readValue(decodedPayload, Map::class.java) as Map<String, Any>
            payloadMap["iss"] as String
        } catch (e: Exception) {
            throw IllegalArgumentException() //todo: 커스텀 예외로 변경
        }
    }

    private fun validateTokenFormat(token: String) {
        val parts = token.split(".")

        if (parts.size != 3) {
            throw IllegalArgumentException() //todo: 커스텀 예외로 변경
        }
    }

    private fun getOidcDecodePayload(token: String, issuer: String): OidcDecodePayload {
        return when {
            issuer.contains("kakao") -> oauthOidcHelper.getKakaoOIDCDecodePayload(token)
            issuer.contains("google") -> oauthOidcHelper.getGoogleOidcDecodePayload(token)
            else -> throw IllegalArgumentException("Unsupported issuer: $issuer") // todo: 커스텀 예외로 변경
        }
    }
}