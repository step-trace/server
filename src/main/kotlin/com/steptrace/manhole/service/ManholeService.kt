package com.steptrace.manhole.service

import com.steptrace.manhole.dto.ManholeDto
import com.steptrace.manhole.repository.ManholeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ManholeService(
        private val manholeRepository: ManholeRepository
) {
    @Transactional(readOnly = true)
    fun getManholeWithAttachment(id: Long) : ManholeDto {
        return manholeRepository.loadManholeWithAttachmentById(id)
    }

    @Transactional
    fun create(manholeDto: ManholeDto) {
        val manholeId = manholeRepository.saveManhole(manholeDto).id
                ?: throw IllegalStateException("Failed to save manhole") //todo: 커스텀 예외로 변경

        manholeRepository.saveManholeAttachments(manholeId, manholeDto.beforeImageUrls)
    }
}