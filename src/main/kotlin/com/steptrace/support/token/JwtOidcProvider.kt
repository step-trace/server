package com.steptrace.support.token

import com.steptrace.auth.OidcDecodePayload
import com.steptrace.auth.OidcPublicKeyDto
import com.steptrace.exception.InvalidTokenException
import com.steptrace.exception.TokenExpiredException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import org.slf4j.LoggerFactory
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.RSAPublicKeySpec
import java.util.Base64

@Component
class JwtOidcProvider {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun getKidFromTokenHeader(token: String): String {
        val kid = "kid"
        val encodedHeader = getEncodedHeader(token)
        val decodedHeader = getDecodedHeader(encodedHeader)

        return try {
            val jsonObject = JSONObject(decodedHeader)
            jsonObject.get(kid).toString()
        } catch (e: Exception) {
            throw InvalidTokenException()
        }
    }

    fun getOIDCTokenJws(token: String, oidcPublicKeyDto: OidcPublicKeyDto, iss: String, aud: String): Jws<Claims> {
        return try {
            Jwts.parser()
                    .verifyWith(getRSAPublicKey(oidcPublicKeyDto.n, oidcPublicKeyDto.e))
                    .requireAudience(aud)
                    .requireIssuer(iss)
                    .build()
                    .parseSignedClaims(token)
        } catch (e: ExpiredJwtException) {
            throw TokenExpiredException()
        } catch (e: Exception) {
            logger.error(e.toString())
            throw InvalidTokenException()
        }
    }

    fun getOIDCTokenBody(token: String, oidcPublicKeyDto: OidcPublicKeyDto, iss: String, aud: String): OidcDecodePayload {
        val payload = getOIDCTokenJws(token, oidcPublicKeyDto, iss, aud).payload

        return OidcDecodePayload(
                payload.issuer,
                payload.audience.toString(),
                payload.subject,
                payload.get("email", String::class.java)
        )
    }

    private companion object {
        private fun getDecodedHeader(encodedHeader: String): String {
            val decodedHeaderBytes = Base64.getDecoder().decode(encodedHeader)
            return String(decodedHeaderBytes)
        }

        private fun getEncodedHeader(token: String): String {
            return token.split(".")[0]
        }
    }

    private fun getRSAPublicKey(modulus: String, exponent: String): PublicKey {
        val keyFactory = KeyFactory.getInstance("RSA")
        val decodeN = Base64.getUrlDecoder().decode(modulus)
        val decodeE = Base64.getUrlDecoder().decode(exponent)
        val n = BigInteger(1, decodeN)
        val e = BigInteger(1, decodeE)

        val keySpec = RSAPublicKeySpec(n, e)
        return keyFactory.generatePublic(keySpec)
    }
}