package com.steptrace.manhole.repository

import com.steptrace.exception.IdNotFoundException
import com.steptrace.manhole.code.ProcessStatus
import com.steptrace.manhole.dto.ManholeDto
import com.steptrace.manhole.dto.ManholeEntity
import com.steptrace.manhole.mapper.ManholeMapper.toAfterImageEntity
import com.steptrace.manhole.mapper.ManholeMapper.toBeforeImageEntity
import com.steptrace.manhole.mapper.ManholeMapper.toDto
import com.steptrace.manhole.mapper.ManholeMapper.toEntity
import com.steptrace.manhole.mapper.ManholeMapper.toPendingDto
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ManholeClient(
        private val manholeJpaRepository: ManholeJpaRepository,
        private val manholeAttachmentJpaRepository: ManholeAttachmentJpaRepository
) : ManholeRepository {

    override fun loadManholeById(id: Long) : ManholeEntity {
        return manholeJpaRepository.findById(id).orElseThrow {
            IdNotFoundException("manhole")
        }
    }

    override fun loadManholesWithAttachment(): List<ManholeDto> {
        val manholes = manholeJpaRepository.findAll()

        return manholes.map { manhole ->
            val attachments = manholeAttachmentJpaRepository.findAllByManholeId(manhole.id!!)
            toDto(manhole, attachments)
        }
    }

    override fun loadManholeWithAttachmentById(id: Long): ManholeDto {
        val manholeEntity = loadManholeById(id)

        val manholeAttachments = manholeAttachmentJpaRepository.findAllByManholeId(id)

        return toDto(manholeEntity, manholeAttachments)
    }

    override fun saveManhole(manholeDto: ManholeDto): ManholeEntity {
        return manholeJpaRepository.save(toEntity(manholeDto))
    }

    override fun modifyManholeAfterImages(id: Long, afterImageUrls: List<String>) {
        manholeAttachmentJpaRepository.saveAll(
            afterImageUrls.map { imageUrl ->
                toAfterImageEntity(id, imageUrl)
            }
        )
    }

    override fun modifyManholeStatus(manholeEntity: ManholeEntity, status: ProcessStatus) {
        manholeEntity.status = status.value
        manholeJpaRepository.save(manholeEntity)
    }

    override fun saveManholeAttachments(manholeId: Long, imageUrls: List<String>) {
        imageUrls.forEach { imageUrl -> manholeAttachmentJpaRepository.save(toBeforeImageEntity(manholeId, imageUrl)) }
    }

    override fun loadManholesWithAttachmentsBySub(sub: String): List<ManholeDto> {
        val manholes = manholeJpaRepository.findAllByUserSub(sub)

        return manholes.map { manhole ->
            val attachments = manholeAttachmentJpaRepository.findAllByManholeId(manhole.id!!)
            toDto(manhole, attachments)
        }
    }

    override fun loadPendingManholesWithAttachmentBetween(startDateTime: LocalDateTime, endDateTime: LocalDateTime): List<ManholeDto> {
        return manholeJpaRepository.findByCreatedAtBetween(startDateTime, endDateTime).map { manhole ->
            val attachments = manholeAttachmentJpaRepository.findAllByManholeId(manhole.id!!)
            toPendingDto(manhole, attachments)
        }
    }
}