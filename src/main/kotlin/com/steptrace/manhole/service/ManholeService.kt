package com.steptrace.manhole.service

import com.steptrace.exception.IdNotFoundException
import com.steptrace.exception.ManholeStatusException
import com.steptrace.manhole.code.ProcessStatus
import com.steptrace.manhole.dto.ManholeDto
import com.steptrace.manhole.mapper.ManholeMapper.toEntity
import com.steptrace.manhole.repository.ManholeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ManholeService(
        private val manholeRepository: ManholeRepository
) {

    @Transactional(readOnly = true)
    fun getManholeMarkers(latitude: Double, longitude: Double): List<ManholeDto> {
        return manholeRepository.loadManholesWithAttachment().filter { manhole ->
            manhole.latitude in latitude - LAT_SHIFT..latitude + LAT_SHIFT
                    && manhole.longitude in longitude - LNG_SHIFT..longitude + LNG_SHIFT
        }
    }

    @Transactional(readOnly = true)
    fun getManholeWithAttachment(id: Long): ManholeDto {
        return manholeRepository.loadManholeWithAttachmentById(id)
    }

    @Transactional
    fun create(manholeDto: ManholeDto) {
        val manholeId = manholeRepository.saveManhole(manholeDto).id
                ?: throw IdNotFoundException("manhole")

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

    companion object {
        private const val LAT_SHIFT = 0.01
        private const val LNG_SHIFT = 0.01
    }
}