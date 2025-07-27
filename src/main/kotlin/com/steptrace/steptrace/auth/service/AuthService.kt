package com.steptrace.steptrace.auth.service

import com.steptrace.steptrace.auth.OidcDecodePayload
import com.steptrace.steptrace.auth.dto.kakao.KakaoTokenDto
import com.steptrace.steptrace.auth.userAccount.dto.UserAccountDto
import com.steptrace.steptrace.auth.userAccount.repository.UserAccountRepository
import com.steptrace.steptrace.support.token.OauthOidcHelper
import com.steptrace.steptrace.support.token.kakao.KakaoInfoClient
import com.steptrace.steptrace.support.token.kakao.KakaoOauthClient
import com.steptrace.steptrace.support.token.kakao.KakaoProperties
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class AuthService(
        private val kakaoOauthClient: KakaoOauthClient,
        private val kakaoInfoClient: KakaoInfoClient,
        private val kakaoProperties: KakaoProperties,
        private val oauthOidcHelper: OauthOidcHelper,
        private val userAccountRepository: UserAccountRepository
) {

    fun getKakaoToken(code: String): KakaoTokenDto {
        return kakaoOauthClient.getKakaoToken(
                kakaoProperties.restApiKey,
                kakaoProperties.redirectUri,
                code,
                kakaoProperties.grantType
        )
    }

    fun isUserRegistered(kakaoTokenDto: KakaoTokenDto): OidcDecodePayload {
        val oidcDecodePayload = oauthOidcHelper.getKakaoOIDCDecodePayload(kakaoTokenDto.idToken)
        val userAccount = userAccountRepository.findBySub(oidcDecodePayload.sub)

        if (userAccount == null) {
            saveUserAccount(kakaoTokenDto)
        }

        return oidcDecodePayload
    }

    private fun saveUserAccount(kakaoTokenDto: KakaoTokenDto) {
        val kakaoInformationResponse = kakaoInfoClient.kakaoUserInfo(
                "${kakaoTokenDto.tokenType} ${kakaoTokenDto.accessToken}"
        )

        val userAccountDto = UserAccountDto.of(
                kakaoInformationResponse.nickname,
                kakaoInformationResponse.sub
        )

        userAccountRepository.save(userAccountDto.toEntity())
    }
}
