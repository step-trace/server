package com.steptrace.auth.service

import com.steptrace.auth.OidcDecodePayload
import com.steptrace.auth.dto.UserInfoDto
import com.steptrace.auth.dto.google.GoogleTokenDto
import com.steptrace.auth.dto.kakao.KakaoTokenDto
import com.steptrace.auth.userAccount.dto.UserAccountDto
import com.steptrace.auth.userAccount.repository.UserAccountRepository
import com.steptrace.support.token.OauthOidcHelper
import com.steptrace.support.token.google.GoogleInfoClient
import com.steptrace.support.token.google.GoogleOauthClient
import com.steptrace.support.token.google.GoogleProperties
import com.steptrace.support.token.kakao.KakaoInfoClient
import com.steptrace.support.token.kakao.KakaoOauthClient
import com.steptrace.support.token.kakao.KakaoProperties
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class AuthService(
        private val kakaoOauthClient: KakaoOauthClient,
        private val kakaoInfoClient: KakaoInfoClient,
        private val kakaoProperties: KakaoProperties,
        private val googleOauthClient: GoogleOauthClient,
        private val googleProperties: GoogleProperties,
        private val googleInfoClient: GoogleInfoClient,
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

    fun isKakaoUserRegistered(kakaoTokenDto: KakaoTokenDto) {
        val oidcDecodePayload = oauthOidcHelper.getKakaoOIDCDecodePayload(kakaoTokenDto.idToken)

        if (!checkUserAccountExists(oidcDecodePayload)) {
            saveUserAccount(getKakaoUserInfo(kakaoTokenDto))
        }
    }


    fun getGoogleToken(code: String): GoogleTokenDto {
        return googleOauthClient.getGoogleToken(
            code,
            googleProperties.clientId,
            googleProperties.clientSecret,
            googleProperties.redirectUri,
            googleProperties.grantType
        )
    }

    fun isGoogleUserRegistered(googleTokenDto: GoogleTokenDto) {
        val oidcDecodePayload = oauthOidcHelper.getGoogleOidcDecodePayload(googleTokenDto.idToken)

        if (!checkUserAccountExists(oidcDecodePayload)) {
            saveUserAccount(getGoogleUserInfo(googleTokenDto))
        }
    }

    fun deleteUserAccount(sub: String) {
        userAccountRepository.deleteBySub(sub)
    }

    private fun checkUserAccountExists(oidcDecodePayload: OidcDecodePayload): Boolean {
        return userAccountRepository.existsBySub(oidcDecodePayload.sub)
    }

    private fun getKakaoUserInfo(kakaoTokenDto: KakaoTokenDto): UserInfoDto {
        return kakaoInfoClient.kakaoUserInfo("${kakaoTokenDto.tokenType} ${kakaoTokenDto.accessToken}")
    }

    private fun getGoogleUserInfo(googleTokenDto: GoogleTokenDto): UserInfoDto {
        return googleInfoClient.googleUserInfo("${googleTokenDto.tokenType} ${googleTokenDto.accessToken}")
    }

    private fun saveUserAccount(userInfoDto: UserInfoDto) {
        val userAccountDto = UserAccountDto.from(userInfoDto)

        userAccountRepository.save(userAccountDto.toEntity())
    }
}
