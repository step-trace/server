package com.steptrace.support.token

import com.steptrace.auth.OidcDecodePayload
import com.steptrace.auth.OidcPublicKeysResponse
import com.steptrace.support.token.google.GoogleOIDCClient
import com.steptrace.support.token.google.GoogleProperties
import com.steptrace.support.token.kakao.KakaoOauthClient
import com.steptrace.support.token.kakao.KakaoProperties
import org.springframework.stereotype.Component

@Component
class OauthOidcHelper(
        private val jwtOidcProvider: JwtOidcProvider,
        private val kakaoOauthClient: KakaoOauthClient,
        private val kakaoProperties: KakaoProperties,
        private val googleOIDCClient: GoogleOIDCClient,
        private val googleProperties: GoogleProperties
) {
    private fun getPayloadFromIdToken(
            token: String,
            iss: String,
            aud: String,
            oidcPublicKeysResponse: OidcPublicKeysResponse
    ): OidcDecodePayload {
        val kid = jwtOidcProvider.getKidFromTokenHeader(token)

        val oidcPublicKeyDto = oidcPublicKeysResponse.keys
                .first { it.kid == kid }

        return jwtOidcProvider.getOIDCTokenBody(token, oidcPublicKeyDto, iss, aud)
    }

    fun getKakaoOIDCDecodePayload(token: String): OidcDecodePayload {
        val oidcPublicKeysResponse = kakaoOauthClient.getKakaoOIDCOpenKeys()

        return getPayloadFromIdToken(
                token,
                kakaoProperties.baseUrl,
                kakaoProperties.restApiKey,
                oidcPublicKeysResponse
        )
    }

    fun getGoogleOidcDecodePayload(token: String): OidcDecodePayload {
        val oidcPublicKeysResponse = googleOIDCClient.getGoogleOIDCOpenKeys()

        return getPayloadFromIdToken(
                token,
                googleProperties.iss,
                googleProperties.clientId,
                oidcPublicKeysResponse
        )

    }
}