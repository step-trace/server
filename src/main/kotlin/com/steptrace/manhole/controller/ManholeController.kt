package com.steptrace.manhole.controller

import com.steptrace.config.security.CustomUserDetails
import com.steptrace.manhole.code.ProcessStatus
import com.steptrace.manhole.dto.*
import com.steptrace.manhole.mapper.ManholeMapper.toDto
import com.steptrace.manhole.service.ManholeService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

/**
 * 맨홀 관리 REST API 컨트롤러
 * 
 * 맨홀 신고, 조회, 수정 및 위치 기반 FCM 푸시 알림을 위한
 * REST API 엔드포인트를 제공합니다. 모바일 앱과의 주요 인터페이스역할을 수행합니다.
 */
@RestController
@RequestMapping("/api")
class ManholeController(
        private val manholeService: ManholeService  // 맨홀 비즈니스 로직 서비스
) {

    /**
     * 지도에 표시할 맨홀 마커들을 조회합니다.
     * 
     * 사용자의 현재 위치를 기준으로 주변 1km 반경 내에 있는
     * 모든 맨홀들을 마커 형태로 반환합니다.
     * 
     * @param latitude 사용자 현재 위도
     * @param longitude 사용자 현재 경도
     * @return 주변 맨홀 마커 목록
     */
    @GetMapping("/v1/manholes")
    fun manholeMarkers(
            @RequestParam latitude: Double,
            @RequestParam longitude: Double,
    ) : List<ManholeMakerResponse> {
        return manholeService.getManholeMarkers(latitude, longitude).map {
            ManholeMakerResponse.from(it)  // 마커 형태로 변환
        }
    }

    /**
     * 맨홀 상세 정보를 상태별로 조회합니다.
     * 
     * 맨홀의 현재 상태(PENDING/REPORTED/COMPLETED)에 따라
     * 다른 형태의 응답 객체를 반환합니다.
     * 
     * @param id 조회할 맨홀 ID
     * @return 맨홀 상태에 따른 상세 정보 응답
     */
    @GetMapping("/v1/manholes/{id}")
    fun processingManhole(
            @PathVariable id: Long
    ) : ManholeResponse {
        val manhole = manholeService.getManholeWithAttachment(id)

        // 맨홀 상태에 따라 다른 응답 타입 반환
        return when(manhole.status) {
            ProcessStatus.PENDING -> PendingManholeResponse.from(manholeService.getManholeWithAttachment(id))      // 신고 대기 상태
            ProcessStatus.REPORTED -> ReportedManholeResponse.from(manholeService.getManholeWithAttachment(id))    // 리포트 생성 완료
            ProcessStatus.COMPLETED -> CompletedManholeResponse.from(manholeService.getManholeWithAttachment(id))  // 수리 완료
        }
    }

    /**
     * 새로운 맨홀을 신고합니다.
     * 
     * 인증된 사용자가 위험한 맨홀을 발견했을 때 신고할 수 있습니다.
     * 신고 시 촬영한 이전 상태 이미지와 위치 정보, 설명을 포함합니다.
     * 
     * @param manholeRequest 맨홀 신고 요청 데이터
     * @param userAccount 인증된 사용자 정보
     */
    @PostMapping("/v1/manholes")
    fun manholes(
            @RequestBody manholeRequest: ManholeRequest,
            @AuthenticationPrincipal userAccount: CustomUserDetails
    ) {
        manholeService.create(toDto(manholeRequest, userAccount.password))  // DTO 변환 후 생성
    }

    /**
     * 맨홀 수리 완료 이미지를 등록합니다.
     * 
     * 작업자가 맨홀 수리를 완료한 후 촬영한 이후 상태 이미지를 등록하고
     * 맨홀 상태를 COMPLETED로 업데이트합니다.
     * 
     * @param id 완료 처리할 맨홀 ID
     * @param afterImageRegistrationRequest 수리 완료 후 이미지 URL 목록
     */
    @PostMapping("/v1/manholes/completed/images/{id}")
    fun completedManholeImages(
            @PathVariable id: Long,
            @RequestBody afterImageRegistrationRequest: AfterImageRegistrationRequest
    ) {
        manholeService.addCompletedManholeImages(id, afterImageRegistrationRequest.afterImageUrl)
    }

    /**
     * 내가 신고한 맨홀 목록을 조회합니다.
     * 
     * 로그인된 사용자가 지금까지 신고한 모든 맨홀들을
     * 상태와 기본 정보와 함께 목록 형태로 반환합니다.
     * 
     * @param userAccount 로그인된 사용자 정보
     * @return 내가 신고한 맨홀 목록
     */
    @GetMapping("/v1/manholes/my-reports")
    fun myReportManholes(
            @AuthenticationPrincipal userAccount: CustomUserDetails
    ) : List<ManholesFromMyReportResponse> {
        return manholeService.getManholesFromMyReport(userAccount.password)
                .map { ManholesFromMyReportResponse.from(it) }  // 목록 형태로 변환
    }

    /**
     * 내가 신고한 맨홀의 상세 정보를 상태별로 조회합니다.
     * 
     * 내 신고 내역에서 특정 맨홀의 상세 정보를 조회할 때 사용하며,
     * 맨홀 상태에 따라 다른 정보를 포함한 응답을 반환합니다.
     * 
     * @param id 조회할 맨홀 ID
     * @return 맨홀 상태에 따른 내 신고 내역 상세 정보
     */
    @GetMapping("/v1/manholes/my-reports/{id}")
    fun myReportProcessingManhole(
            @PathVariable id: Long
    ) : ManholeFromMyReportResponse {
        val manhole = manholeService.getManholeWithAttachment(id)

        // 맨홀 상태에 따라 다른 내 신고 내역 응답 타입 반환
        return when(manhole.status) {
            ProcessStatus.PENDING -> PendingManholeFromMyReportResponse.from(manhole)      // 신고 대기 상태
            ProcessStatus.REPORTED -> ReportedManholeFromMyReportResponse.from(manhole)    // 리포트 생성 완료
            ProcessStatus.COMPLETED -> CompletedManholeFromMyReportResponse.from(manhole)  // 수리 완료
        }
    }

    /**
     * 위치 기반 FCM 푸시 알림을 전송합니다.
     * 
     * 모바일 앱이 사용자의 현재 위치를 전송하면, 주변에 위험한 맨홀이 있을 경우
     * 안전 알림을 푸시합니다. Redis 캐싱을 통해 중복 알림을 방지합니다.
     * 
     * @param pushRequest FCM 토큰과 사용자 위치 정보
     */
    @PostMapping("/v1/manholes/push/fcm")
    fun pushFcm(
            @RequestBody pushRequest: PushRequest
    ) {
        manholeService.pushFcm(pushRequest)
    }
}
