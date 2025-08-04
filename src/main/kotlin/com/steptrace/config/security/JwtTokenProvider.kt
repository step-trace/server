package com.steptrace.config.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.steptrace.auth.dto.UserInfoDto
import com.steptrace.support.token.TokenProperties
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*
@Component
class JwtTokenProvider(
        private val tokenProperties: TokenProperties
) {
    private val log = LoggerFactory.getLogger(JwtTokenProvider::class.java)

    fun create(user: UserInfoDto): String {
        val jwt = JWT.create()
                .withSubject(user.sub)
                .withExpiresAt(Date(System.currentTimeMillis() + tokenProperties.expiration.toLong()))
                .withClaim("email", user.email)
                .withClaim("name", user.name)
                .withClaim("nickname", user.nickname)
                .withIssuer(tokenProperties.issuer)
                .sign(Algorithm.HMAC512(tokenProperties.secret))

        return "${tokenProperties.prefix} $jwt"
    }

    fun verify(jwt: String): DecodedJWT = JWT.require(Algorithm.HMAC512(tokenProperties.secret))
            .build()
            .verify(jwt.replace(tokenProperties.secret, ""))
}

