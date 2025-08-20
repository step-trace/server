package com.steptrace.auth.service

import com.steptrace.auth.OidcDecodePayload
import com.steptrace.auth.dto.TokenDto
import com.steptrace.auth.dto.LoginResponse
import com.steptrace.auth.dto.UserInfoDto
import com.steptrace.auth.userAccount.dto.UserAccountDto
import com.steptrace.auth.userAccount.repository.UserAccountRepository
import com.steptrace.config.security.JwtTokenProvider
import com.steptrace.support.token.OauthOidcHelper
import com.steptrace.support.token.google.GoogleInfoClient
import com.steptrace.support.token.kakao.KakaoInfoClient
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * OAuth 2.0/OIDC 인증 서비스
 * 
 * Google 및 Kakao OAuth 인증을 통해 사용자 인증 및 등록을 처리합니다.
 * OIDC 토큰 검증, 사용자 정보 추출, JWT 토큰 생성을 담당합니다.
 */
@Transactional
@Service
class AuthService(
        private val kakaoInfoClient: KakaoInfoClient,            // Kakao 사용자 정보 API 클라이언트
        private val googleInfoClient: GoogleInfoClient,          // Google 사용자 정보 API 클라이언트
        private val oauthOidcHelper: OauthOidcHelper,            // OIDC 토큰 검증 도우미
        private val userAccountRepository: UserAccountRepository, // 사용자 어카운트 데이터 접근
        private val jwtTokenProvider: JwtTokenProvider           // JWT 토큰 생성기
) {

    /**
     * Kakao OAuth 로그인 처리
     * 
     * Kakao에서 발급받은 ID 토큰을 검증하고, 사용자 정보를 추출합니다.
     * 첫 로그인 사용자는 자동으로 등록되며, JWT 토큰을 발급합니다.
     * 
     * @param tokenDto Kakao OAuth 토큰 정보 (ID 토큰 포함)
     * @return 로그인 응답 (JWT 토큰 및 사용자 정보)
     */
    fun isKakaoUserRegistered(tokenDto: TokenDto): LoginResponse {
        val oidcDecodePayload = oauthOidcHelper.getKakaoOIDCDecodePayload(tokenDto.idToken)  // OIDC ID 토큰 검증 및 디코딩
        val kakaoUserInfoDto = getKakaoUserInfo(tokenDto)  // Kakao로부터 사용자 정보 가져오기

        // 첫 로그인 사용자라면 데이터베이스에 등록
        if (!checkUserAccountExists(oidcDecodePayload)) {
            saveUserAccount(kakaoUserInfoDto)
        }

        // JWT 토큰 생성 및 로그인 응답 반환
        return LoginResponse.of(jwtTokenProvider.create(kakaoUserInfoDto), kakaoUserInfoDto)
    }

    /**
     * Google OAuth 로그인 처리
     * 
     * Google에서 발급받은 ID 토큰을 검증하고, 사용자 정보를 추출합니다.
     * 첫 로그인 사용자는 자동으로 등록되며, JWT 토큰을 발급합니다.
     * 
     * @param tokenDto Google OAuth 토큰 정보 (ID 토큰 포함)
     * @return 로그인 응답 (JWT 토큰 및 사용자 정보)
     */
    fun isGoogleUserRegistered(tokenDto: TokenDto): LoginResponse {
        val oidcDecodePayload = oauthOidcHelper.getGoogleOidcDecodePayload(tokenDto.idToken)  // OIDC ID 토큰 검증 및 디코딩
        val googleUserInfoDto = getGoogleUserInfo(tokenDto)  // Google로부터 사용자 정보 가져오기

        // 첫 로그인 사용자라면 데이터베이스에 등록
        if (!checkUserAccountExists(oidcDecodePayload)) {
            saveUserAccount(googleUserInfoDto)
        }

        // JWT 토큰 생성 및 로그인 응답 반환
        return LoginResponse.of(jwtTokenProvider.create(googleUserInfoDto), googleUserInfoDto)
    }

    /**
     * 사용자 계정을 삭제합니다.
     * 
     * JWT 토큰의 sub 클레임을 기준으로 해당 사용자 계정을 데이터베이스에서 완전히 삭제합니다.
     * 이 작업은 되돌릴 수 없으므로 주의가 필요합니다.
     * 
     * @param sub 삭제할 사용자의 식별자 (JWT sub 클레임)
     */
    fun deleteUserAccount(sub: String) {
        userAccountRepository.deleteBySub(sub)
    }

    /**
     * 사용자 계정 존재 여부를 확인합니다.
     * 
     * OIDC 페이로드의 sub 클레임을 이용하여 데이터베이스에
     * 해당 사용자가 이미 등록되어 있는지 확인합니다.
     * 
     * @param oidcDecodePayload 디코딩된 OIDC ID 토큰 정보
     * @return 계정 존재 여부
     */
    private fun checkUserAccountExists(oidcDecodePayload: OidcDecodePayload): Boolean {
        return userAccountRepository.existsBySub(oidcDecodePayload.sub)
    }

    /**
     * Kakao API로부터 사용자 정보를 가져옵니다.
     * 
     * 액세스 토큰을 사용하여 Kakao 사용자 정보 API를 호출하고
     * 사용자의 기본 프로필 정보를 가져옵니다.
     * 
     * @param tokenDto Kakao OAuth 토큰 정보
     * @return Kakao 사용자 정보
     */
    private fun getKakaoUserInfo(tokenDto: TokenDto): UserInfoDto {
        return kakaoInfoClient.kakaoUserInfo("${tokenDto.tokenType} ${tokenDto.accessToken}")
    }

    /**
     * Google API로부터 사용자 정보를 가져옵니다.
     * 
     * 액세스 토큰을 사용하여 Google 사용자 정보 API를 호출하고
     * 사용자의 기본 프로필 정보를 가져옵니다.
     * 
     * @param tokenDto Google OAuth 토큰 정보
     * @return Google 사용자 정보
     */
    private fun getGoogleUserInfo(tokenDto: TokenDto): UserInfoDto {
        return googleInfoClient.googleUserInfo("${tokenDto.tokenType} ${tokenDto.accessToken}")
    }

    /**
     * 사용자 계정을 데이터베이스에 저장합니다.
     * 
     * OAuth 제공자로부터 받은 사용자 정보를 UserAccountDto로 변환한 후
     * JPA 엔티티로 변환하여 데이터베이스에 새 사용자로 등록합니다.
     * 
     * @param userInfoDto OAuth 제공자로부터 받은 사용자 정보
     */
    private fun saveUserAccount(userInfoDto: UserInfoDto) {
        val userAccountDto = UserAccountDto.from(userInfoDto)  // DTO 변환

        userAccountRepository.save(userAccountDto.toEntity())  // JPA 엔티티로 변환 후 저장
    }
}
