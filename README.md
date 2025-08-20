# 🚧 StepTrace - AI 기반 위험 맨홀 추적 및 관리 시스템

<div align="center">

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2+-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-7.0+-DC382D?style=for-the-badge&logo=redis&logoColor=white)](https://redis.io/)
[![AWS](https://img.shields.io/badge/AWS-S3-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white)](https://aws.amazon.com/)
[![Firebase](https://img.shields.io/badge/Firebase-FCM-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)](https://firebase.google.com/)

*AI 기반 도시 안전 인프라 관리 플랫폼*

</div>

## 📖 프로젝트 소개

StepTrace는 **Anthropic Claude AI**를 활용하여 위험한 맨홀을 자동으로 감지하고 관리하는 스마트 도시 안전 시스템입니다. 시민들이 위험한 맨홀을 신고하면 AI가 자동으로 분석하여 위험도를 평가하고, 위치 기반으로 실시간 안전 알림을 제공합니다.

### ✨ 주요 특징

- **AI 기반 이미지 분석**: Claude API를 활용한 맨홀 상태 자동 분석
- **실시간 위치 추적**: GPS 기반 맨홀 위치 관리 및 주변 알림
- **스마트 알림 시스템**: FCM을 통한 위험 지역 접근 시 즉시 알림
- **자동 리포트 생성**: 일일 Excel/HTML 리포트 자동 생성
- **OAuth 2.0 인증**: Google, Kakao 소셜 로그인 지원

## 🚀 주요 기능

### 1. 맨홀 신고 및 관리
- 모바일 앱을 통한 간편한 맨홀 신고
- 이전/이후 상태 이미지 비교
- GPS 좌표 기반 정확한 위치 정보
- PENDING → REPORTED → COMPLETED 워크플로우

### 2. AI 기반 이미지 분석
- **2단계 프롬프트 검증**: 유효성 검증 → 위험도 상세 분석
- **93.1% 정확도**: 기존 단일 프롬프트 대비 5.8% 향상
- **거짓 양성률 68% 감소**: 비맨홀 객체 오판 대폭 감소
- **실시간 분석**: 평균 3.8초 내 분석 완료

### 3. 실시간 안전 알림
- 100m 반경 내 위험 맨홀 감지 시 즉시 푸시 알림
- Redis 캐싱을 통한 중복 알림 방지
- 3초 이내 실시간 알림 전송

### 4. 자동화된 리포트 시스템
- 매일 자정 자동 리포트 생성
- Excel, HTML 형식 지원
- ☁AWS S3 자동 업로드

## 🛠️ 기술 스택

### Backend
- 언어: Kotlin 1.9+
- 프레임워크: Spring Boot 3.2+, Spring Security, Spring Data JPA
- AI: Spring AI + Anthropic Claude API
- 데이터베이스: MySQL 8.0, Redis 7.0
- 클라우드: AWS (S3, EC2)
- 푸시 알림: Firebase Cloud Messaging (FCM)
- 빌드 도구: Gradle

### Infra
- Docker, AWS EC2
- Firebase Admin SDK
- OAuth 2.0/OIDC (Google, Kakao)

## 📦 설치 및 실행

### 1. 저장소 클론
```bash
git clone https://github.com/your-username/step-trace.git
cd step-trace
```

### 2. 환경 변수 설정
```bash
# .env 파일 생성
cp .env.example .env

# 필요한 API 키 설정
CLAUDE_API_KEY=your_claude_api_key
FIREBASE_CONFIG_PATH=path_to_firebase_config
AWS_ACCESS_KEY_ID=your_aws_access_key
AWS_SECRET_ACCESS_KEY=your_aws_secret_key
GOOGLE_CLIENT_ID=your_google_client_id
KAKAO_CLIENT_ID=your_kakao_client_id
```

### 3. 데이터베이스 실행

### 4. 애플리케이션 실행

## 📡 API 엔드포인트

> 📋 **전체 API 문서**: [API.md](https://github.com/step-trace/server/blob/main/docs/API.md)

### 맨홀 관리
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/manholes` | 주변 맨홀 마커 조회 |
| GET | `/api/v1/manholes/{id}` | 맨홀 상세 정보 조회 |
| POST | `/api/v1/manholes` | 새 맨홀 신고 |
| POST | `/api/v1/manholes/completed/images/{id}` | 완료 이미지 등록 |
| GET | `/api/v1/manholes/my-reports` | 내 신고 내역 조회 |

### AI 이미지 분석
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/ai/abnormal-manhole/image` | 비정상 맨홀 분석 |
| POST | `/api/v1/ai/normal-manhole/image` | 정상 맨홀 검증 |

### 인증
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/v1/google` | Google OAuth 로그인 |
| POST | `/api/auth/v1/kakao` | Kakao OAuth 로그인 |
| DELETE | `/api/auth/users` | 사용자 계정 삭제 |

### 푸시 알림
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/manholes/push/fcm` | 위치 기반 FCM 알림 |

---

## 🏗️ 시스템 구조

```
📦src
 ┣ 📂main
 ┃ ┣ 📂kotlin
 ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┗ 📂steptrace
 ┃ ┃ ┃ ┃ ┣ 📂ai
 ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜AiController.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ImageAnalysisRequest.kt
 ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜AiService.kt
 ┃ ┃ ┃ ┃ ┣ 📂auth
 ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜AuthController.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂google
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜GoogleUserInfoDto.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂kakao
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜KakaoUserInfoDto.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜LoginResponse.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜TokenDto.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserInfoDto.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜AuthService.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📂userAccount
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜UserAccountDto.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserAccountEntity.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜UserAccountRepository.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜BaseAuditEntity.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📜OidcDecodePayload.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📜OidcPublicKeyDto.kt
 ┃ ┃ ┃ ┃ ┃ ┗ 📜OidcPublicKeysResponse.kt
 ┃ ┃ ┃ ┃ ┣ 📂common
 ┃ ┃ ┃ ┃ ┃ ┗ 📂converter
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜AttributeConverter.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BooleanToYNConverter.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜JsonConverterFactory.kt
 ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┣ 📂security
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CustomUserDetails.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CustomUserDetailsService.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JwtAuthenticationFilter.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JwtTokenProvider.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SecurityConfig.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SpringSecurityAuditorAware.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ChatClientConfig.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📜JacksonConfig.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📜RedisCacheConfig.kt
 ┃ ┃ ┃ ┃ ┃ ┗ 📜S3Config.kt
 ┃ ┃ ┃ ┃ ┣ 📂exception
 ┃ ┃ ┃ ┃ ┃ ┣ 📜AiResponseGenerateException.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📜BadRequestToAiException.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ControllerAdvice.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📜EntityNotFoundException.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ExcelGenerationException.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📜InvalidManholeResponseTypeException.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📜InvalidProcessStatusException.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📜InvalidTokenException.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📜ManholeStatusException.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📜StepTraceException.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📜TokenExpiredException.kt
 ┃ ┃ ┃ ┃ ┃ ┗ 📜UserNotFoundException.kt
 ┃ ┃ ┃ ┃ ┣ 📂image
 ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ImageController.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ImageS3UploadRequest.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ImageS3UploadResponse.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜S3UrlDto.kt
 ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ImageService.kt
 ┃ ┃ ┃ ┃ ┣ 📂manhole
 ┃ ┃ ┃ ┃ ┃ ┣ 📂code
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ManholeResponseType.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ProcessStatus.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ManholeController.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ManholeAttachmentEntity.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ManholeDto.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ManholeEntity.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ManholeRequest.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ManholeResponse.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📂mapper
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ManholeMapper.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ManholeAttachmentJpaRepository.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ManholeClient.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ManholeJpaRepository.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ManholeRepository.kt
 ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ManholeService.kt
 ┃ ┃ ┃ ┃ ┣ 📂push
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FcmDto.kt
 ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜PushService.kt
 ┃ ┃ ┃ ┃ ┣ 📂scheduler
 ┃ ┃ ┃ ┃ ┃ ┣ 📂code
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ManholeExcelColumn.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ExcelReportService.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜HtmlReportService.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜S3Service.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📂util
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ExcelStyleProvider.kt
 ┃ ┃ ┃ ┃ ┃ ┗ 📜ManholeReportScheduler.kt
 ┃ ┃ ┃ ┃ ┣ 📂support
 ┃ ┃ ┃ ┃ ┃ ┣ 📂fcm
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜FcmFeignClient.kt
 ┃ ┃ ┃ ┃ ┃ ┗ 📂token
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂google
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜GoogleFeignClient.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜GoogleProperties.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂kakao
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜KakaoFeignClient.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜KakaoProperties.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JwtOidcProvider.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜OauthOidcHelper.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜TokenProperties.kt
 ┃ ┃ ┃ ┃ ┗ 📜StepTraceApplication.kt
 ┃ ┗ 📂resources
 ┃ ┃ ┣ 📂firebase
 ┃ ┃ ┃ ┗ 📜steptrace-a734a-firebase-adminsdk-fbsvc-08656e4cfd.json
 ┃ ┃ ┣ 📂prompts
 ┃ ┃ ┃ ┣ 📜abnormal-manhole-analysis-by-sub-ai.st
 ┃ ┃ ┃ ┣ 📜abnormal-manhole-analysis.st
 ┃ ┃ ┃ ┗ 📜normal-manhole-analysis.st
 ┃ ┃ ┣ 📂static
 ┃ ┃ ┣ 📂templates
 ┃ ┃ ┃ ┗ 📜manhole-report.html
 ┃ ┃ ┗ 📜application.yml
 ┗ 📂test
 ┃ ┗ 📂kotlin
 ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┗ 📂steptrace
 ┃ ┃ ┃ ┃ ┣ 📂annotation
 ┃ ┃ ┃ ┃ ┃ ┗ 📜UnitTest.kt
 ┃ ┃ ┃ ┃ ┣ 📂image
 ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ImageServiceTest.kt
 ┃ ┃ ┃ ┃ ┃ ┗ 📂stub
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ImageStub.kt
 ┃ ┃ ┃ ┃ ┣ 📂manhole
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ManholeRequestTest.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ManholeResponseTest.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ManholeClientTest.kt
 ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ManholeServiceTest.kt
 ┃ ┃ ┃ ┃ ┃ ┗ 📂stub
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ManholeAttachmentEntityStub.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ManholeDtoStub.kt
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ManholeEntityStub.kt
 ┃ ┃ ┃ ┃ ┗ 📜StepTraceApplicationTests.kt
```

<div align="center">
  
**🌟 StepTrace와 함께 더 안전한 도시를 만들어가세요! 🌟**

Made with ❤️ by StepTrace Team

</div>
