package com.steptrace.manhole.service

import com.steptrace.exception.EntityNotFoundException
import com.steptrace.exception.ManholeStatusException
import com.steptrace.manhole.code.ProcessStatus
import com.steptrace.manhole.dto.ManholeDto
import com.steptrace.manhole.dto.PushRequest
import com.steptrace.manhole.mapper.ManholeMapper.toEntity
import com.steptrace.manhole.repository.ManholeRepository
import com.steptrace.push.dto.FcmDto
import com.steptrace.push.service.PushService
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.math.abs

/**
 * 맨홀 관리 서비스
 * 
 * 맨홀의 생성, 수정, 조회 및 위치 기반 푸시 알림을 관리하는 핵심 비즈니스 로직을 처리합니다.
 * Redis 캐싱을 통해 중복 알림을 방지하고, 거리 기반 필터링으로 관련 맨홀만 조회합니다.
 */
@Service
class ManholeService(
        private val manholeRepository: ManholeRepository,  // 맨홀 데이터 저장소 접근
        private val pushService: PushService,              // FCM 푸시 알림 서비스
        private val redisTemplate: RedisTemplate<String, String>  // 위치 캐싱용 Redis
) {

    /**
     * 지정된 위치 주변의 맨홀 마커들을 조회합니다.
     * 
     * 위도/경도 기준으로 0.01도 범위 내의 맨홀들을 반환하며,
     * 지도에서 마커 표시용으로 사용됩니다.
     * 
     * @param latitude 중심 위도
     * @param longitude 중심 경도
     * @return 주변 맨홀 정보 목록
     */
    @Transactional(readOnly = true)
    fun getManholeMarkers(latitude: Double, longitude: Double): List<ManholeDto> {
        return getNearbyManholes(latitude, longitude, LAT_SHIFT, LNG_SHIFT)
    }

    /**
     * ID로 맨홀 상세 정보를 첨부파일과 함께 조회합니다.
     * 
     * @param id 조회할 맨홀 ID
     * @return 첨부파일이 포함된 맨홀 상세 정보
     */
    @Transactional(readOnly = true)
    fun getManholeWithAttachment(id: Long): ManholeDto {
        return manholeRepository.loadManholeWithAttachmentById(id)
    }

    /**
     * 새로운 맨홀을 생성합니다.
     * 
     * 맨홀 기본 정보를 저장한 후, 신고 시 촬영한 이전 상태 이미지들을
     * 첨부파일로 함께 저장합니다.
     * 
     * @param manholeDto 생성할 맨홀 정보 (이전 상태 이미지 URL 포함)
     * @throws EntityNotFoundException 맨홀 생성 실패 시
     */
    @Transactional
    fun create(manholeDto: ManholeDto) {
        val manholeId = manholeRepository.saveManhole(manholeDto).id
                ?: throw EntityNotFoundException("manhole")

        // 신고 시 촬영한 이전 상태 이미지들을 첨부파일로 저장
        manholeRepository.saveManholeAttachments(manholeId, manholeDto.beforeImageUrls)
    }

    /**
     * 맨홀 완료 이미지들을 추가하고 상태를 COMPLETED로 변경합니다.
     * 
     * 수리 완료 후 촬영한 이후 상태 이미지들을 저장하고,
     * 맨홀의 처리 상태를 COMPLETED로 업데이트합니다.
     * 
     * @param id 완료 처리할 맨홀 ID
     * @param afterImageUrls 수리 완료 후 이미지 URL 목록
     * @throws ManholeStatusException 이미 처리된 맨홀인 경우
     */
    @Transactional
    fun addCompletedManholeImages(id: Long, afterImageUrls: List<String>) {
        val manhole = manholeRepository.loadManholeWithAttachmentById(id)
                .takeIf { it.afterImageUrls.isNullOrEmpty() }  // 아직 완료 이미지가 없는 경우만 처리
                ?: throw ManholeStatusException("이미 처리된 맨홀입니다.")

        manholeRepository.modifyManholeAfterImages(manhole.id!!, afterImageUrls)  // 완료 이미지 저장
        manholeRepository.modifyManholeStatus(toEntity(manhole), ProcessStatus.COMPLETED)  // 상태를 COMPLETED로 변경
    }

    /**
     * 사용자가 신고한 맨홀 목록을 조회합니다.
     * 
     * JWT 토큰의 sub 클레임을 기준으로 해당 사용자가 신고한
     * 모든 맨홀들을 첨부파일과 함께 반환합니다.
     * 
     * @param sub 사용자 식별자 (JWT sub 클레임)
     * @return 사용자가 신고한 맨홀 목록
     */
    @Transactional(readOnly = true)
    fun getManholesFromMyReport(sub: String): List<ManholeDto> {
        return manholeRepository.loadManholesWithAttachmentsBySub(sub)
    }

    /**
     * 사용자 위치를 기반으로 FCM 푸시 알림을 전송합니다.
     * 
     * 위지진이나 작업자가 예전에 신고된 위험 맨홀 근처(0.001도 반경)에
     * 다가갔을 때 안전 알림을 보냅니다. Redis 캐싱으로 중복 알림을 방지합니다.
     * 
     * @param pushRequest FCM 토큰과 사용자 위치 정보
     */
    @Transactional(readOnly = true)
    fun pushFcm(pushRequest: PushRequest) {
        // 푸시 알림이 필요한지 체크 (위치 변경 여부 확인)
        if (!shouldSendPushNotification(pushRequest)) {
            return
        }

        // 현재 사용자 위치를 Redis에 캐싱
        updateLocationCache(pushRequest)

        // 주변의 위험 맨홀 검색 (더 좋은 정확도로 0.001도 범위)
        val nearByDangerManhole = getNearbyManholes(
                pushRequest.latitude,
                pushRequest.longitude,
                LAT_SHIFT_FOR_FCM,
                LNG_SHIFT_FOR_FCM
        )

        // 주변에 위험 맨홀이 있으면 알림 전송
        if (nearByDangerManhole.isNotEmpty()) {
            pushService.pushFcm(FcmDto.from(pushRequest.token))
        }
    }

    /**
     * 푸시 알림을 전송해야 하는지 판단합니다.
     * 
     * Redis에 저장된 이전 위치와 비교하여 위치 변경이 있을 때만 알림을 보냅니다.
     * 처음 요청하는 사용자는 무조건 알림을 받습니다.
     * 
     * @param pushRequest 푸시 요청 정보
     * @return 알림 전송 여부
     */
    private fun shouldSendPushNotification(pushRequest: PushRequest): Boolean {
        val cachedLocation = getCachedLocation(pushRequest.token)
                ?: return true  // 캐시된 위치가 없으면 처음 요청이므로 알림 전송

        return hasLocationChanged(pushRequest, cachedLocation)  // 위치 변경 여부 확인
    }

    /**
     * Redis에서 사용자의 이전 위치를 가져옵니다.
     * 
     * FCM 토큰을 키로 사용하여 Redis에 저장된 사용자의 마지막 위치를 검색합니다.
     * 위치 데이터는 "latitude,longitude" 형식의 문자열로 저장됩니다.
     * 
     * @param token 사용자의 FCM 토큰
     * @return 이전 위치 [latitude, longitude] 또는 null
     */
    private fun getCachedLocation(token: String): List<Double>? {
        val cacheKey = "fcm_token:$token"  // Redis 에서 사용할 키 생성
        val cachedLocationString = redisTemplate.opsForValue().get(cacheKey)

        // "latitude,longitude" 형식의 문자열을 Double 리스트로 변환
        return cachedLocationString?.split(",")?.map { it.toDouble() }
    }

    /**
     * 사용자의 위치가 이전과 비교해 의미 있게 변경되었는지 확인합니다.
     * 
     * 위도/경도 각각 0.001도 이상 변경된 경우에만 위치 변경으로 판단하여
     * 불필요한 중복 알림을 방지합니다.
     * 
     * @param pushRequest 현재 요청 위치 정보
     * @param cachedLocation 이전에 캐시된 위치 [latitude, longitude]
     * @return 위치 변경 여부
     */
    private fun hasLocationChanged(
            pushRequest: PushRequest,
            cachedLocation: List<Double>
    ): Boolean {
        val (cachedLat, cachedLng) = cachedLocation
        val latDiff = abs(pushRequest.latitude - cachedLat)   // 위도 차이
        val lngDiff = abs(pushRequest.longitude - cachedLng)  // 경도 차이

        // FCM 알림용 임계값(0.001도)보다 큰 변경이 있을 때만 위치 변경으로 인정
        return latDiff > LAT_SHIFT_FOR_FCM || lngDiff > LNG_SHIFT_FOR_FCM
    }

    /**
     * 사용자의 현재 위치를 Redis에 업데이트합니다.
     * 
     * FCM 토큰을 키로 사용하여 사용자의 최신 위치를 "latitude,longitude" 형식으로 저장합니다.
     * 이를 통해 다음 요청 시 위치 변경 여부를 판단할 수 있습니다.
     * 
     * @param pushRequest 현재 위치 정보를 포함한 푸시 요청
     */
    private fun updateLocationCache(pushRequest: PushRequest) {
        val cacheKey = "fcm_token:${pushRequest.token}"  // Redis 키 생성
        val locationValue = "${pushRequest.latitude},${pushRequest.longitude}"  // 위치 데이터 형식
        redisTemplate.opsForValue().set(cacheKey, locationValue)  // Redis에 위치 정보 저장
    }

    /**
     * 지정된 위치 주변의 맨홀들을 거리 기반으로 필터링하여 반환합니다.
     * 
     * 데이터베이스에서 모든 맨홀들을 가져온 후, 주어진 위도/경도 중심에서
     * 지정된 거리(위도/경도 오프셋) 내에 있는 맨홀들만 필터링합니다.
     * 
     * @param latitude 중심 위도
     * @param longitude 중심 경도 
     * @param latShift 위도 방향 검색 범위 (도 단위)
     * @param lngShift 경도 방향 검색 범위 (도 단위)
     * @return 범위 내에 있는 맨홀 목록
     */
    private fun getNearbyManholes(latitude: Double, longitude: Double, latShift: Double, lngShift: Double): List<ManholeDto> {
        return manholeRepository.loadManholesWithAttachment()
                .filter { manhole ->
                    // 주어진 위도/경도 범위 내에 맨홀이 있는지 확인
                    manhole.latitude in latitude - latShift..latitude + latShift
                            && manhole.longitude in longitude - lngShift..longitude + lngShift
                }
    }

    companion object {
        /**
         * 지도에서 맨홀 마커 표시용 위도 검색 범위 (0.01도 ≈ 약 1km)
         */
        private const val LAT_SHIFT = 0.01
        
        /**
         * 지도에서 맨홀 마커 표시용 경도 검색 범위 (0.01도 ≈ 약 1km)
         */
        private const val LNG_SHIFT = 0.01
        
        /**
         * FCM 푸시 알림용 위도 검색 범위 (0.001도 ≈ 약 100m)
         * 더 정밀한 거리 기반 알림을 위해 작은 범위 사용
         */
        private const val LAT_SHIFT_FOR_FCM = 0.001
        
        /**
         * FCM 푸시 알림용 경도 검색 범위 (0.001도 ≈ 약 100m)
         * 더 정밀한 거리 기반 알림을 위해 작은 범위 사용
         */
        private const val LNG_SHIFT_FOR_FCM = 0.001
    }
}