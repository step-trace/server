package com.steptrace.manhole.repository

import com.steptrace.manhole.dto.ManholeDto
import com.steptrace.manhole.dto.ManholeEntity
import com.steptrace.manhole.mapper.ManholeMapper.toDto
import com.steptrace.manhole.mapper.ManholeMapper.toEntity
import org.springframework.stereotype.Repository

@Repository
class ManholeClient(
        private val manholeJpaRepository: ManholeJpaRepository,
        private val manholeAttachmentJpaRepository: ManholeAttachmentJpaRepository
) : ManholeRepository {

    override fun loadProcessingManholeById(id: Long): ManholeDto {
        val processingManhole = manholeJpaRepository.findById(id).orElseThrow {
            IllegalArgumentException("Manhole with id $id not found") //todo: 커스텀 예외로 변경
        }

        val processingManholeAttachments = manholeAttachmentJpaRepository.findAllByManholeId(id).filterNot { it.isCompleted }

        return toDto(processingManhole, processingManholeAttachments)
    }

    override fun saveManhole(manholeDto: ManholeDto): ManholeEntity {
        return manholeJpaRepository.save(toEntity(manholeDto))
    }

    override fun saveManholeAttachments(manholeId: Long, imageUrls: List<String>) {
        imageUrls.forEach { imageUrl -> manholeAttachmentJpaRepository.save(toEntity(manholeId, imageUrl)) }
    }
}