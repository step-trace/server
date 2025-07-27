package com.steptrace.steptrace.support.token

import com.steptrace.steptrace.auth.OidcDecodePayload
import com.steptrace.steptrace.auth.OidcPublicKeysResponse
import com.steptrace.steptrace.support.token.kakao.KakaoOauthClient
import com.steptrace.steptrace.support.token.kakao.KakaoProperties
import org.springframework.stereotype.Component

@Component
class OauthOidcHelper(
        private val jwtOidcProvider: JwtOidcProvider,
        private val kakaoOauthClient: KakaoOauthClient,
        private val kakaoProperties: KakaoProperties
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
}