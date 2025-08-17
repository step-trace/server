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
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ManholeService(
        private val manholeRepository: ManholeRepository,
        private val pushService: PushService
) {

    @Transactional(readOnly = true)
    fun getManholeMarkers(latitude: Double, longitude: Double): List<ManholeDto> {
        return getNearbyManholes(latitude, longitude, LAT_SHIFT, LNG_SHIFT)
    }

    @Transactional(readOnly = true)
    fun getManholeWithAttachment(id: Long): ManholeDto {
        return manholeRepository.loadManholeWithAttachmentById(id)
    }

    @Transactional
    fun create(manholeDto: ManholeDto) {
        val manholeId = manholeRepository.saveManhole(manholeDto).id
                ?: throw EntityNotFoundException("manhole")

        manholeRepository.saveManholeAttachments(manholeId, manholeDto.beforeImageUrls)
    }

    @Transactional
    fun addCompletedManholeImages(id: Long, afterImageUrls: List<String>) {
        val manhole = manholeRepository.loadManholeWithAttachmentById(id)
                .takeIf { it.afterImageUrls.isNullOrEmpty() }
                ?: throw ManholeStatusException("이미 처리된 맨홀입니다.")

        manholeRepository.modifyManholeAfterImages(manhole.id!!, afterImageUrls)
        manholeRepository.modifyManholeStatus(toEntity(manhole), ProcessStatus.COMPLETED)
    }

    @Transactional(readOnly = true)
    fun getManholesFromMyReport(sub: String): List<ManholeDto> {
        return manholeRepository.loadManholesWithAttachmentsBySub(sub)
    }

    @Transactional(readOnly = true)
    fun pushFcm(pushRequest: PushRequest) {
        val nearByDangerManhole = getNearbyManholes(pushRequest.latitude, pushRequest.longitude, LAT_SHIFT_FOR_FCM, LNG_SHIFT_FOR_FCM)

        if (nearByDangerManhole.isNotEmpty()) {
            pushService.pushFcm(FcmDto.from(pushRequest.token))
        }
    }

    private fun getNearbyManholes(latitude: Double, longitude: Double, latShift: Double, lngShift: Double): List<ManholeDto> {
        return manholeRepository.loadManholesWithAttachment()
                .filter { manhole ->
                    manhole.latitude in latitude - latShift..latitude + latShift
                            && manhole.longitude in longitude - lngShift..longitude + lngShift
                }
    }

    companion object {
        private const val LAT_SHIFT = 0.01
        private const val LNG_SHIFT = 0.01
        private const val LAT_SHIFT_FOR_FCM = 0.001
        private const val LNG_SHIFT_FOR_FCM = 0.001
    }
}