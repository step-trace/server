package com.steptrace.manhole.repository

import com.steptrace.exception.IdNotFoundException
import com.steptrace.manhole.dto.ManholeDto
import com.steptrace.manhole.dto.ManholeEntity
import com.steptrace.manhole.mapper.ManholeMapper.toAfterImageEntity
import com.steptrace.manhole.mapper.ManholeMapper.toBeforeImageEntity
import com.steptrace.manhole.mapper.ManholeMapper.toDto
import com.steptrace.manhole.mapper.ManholeMapper.toEntity
import org.springframework.stereotype.Repository

@Repository
class ManholeClient(
        private val manholeJpaRepository: ManholeJpaRepository,
        private val manholeAttachmentJpaRepository: ManholeAttachmentJpaRepository
) : ManholeRepository {

    override fun loadManholesWithAttachment(): List<ManholeDto> {
        val manholes = manholeJpaRepository.findAll()

        return manholes.map { manhole ->
            val attachments = manholeAttachmentJpaRepository.findAllByManholeId(manhole.id!!)
            toDto(manhole, attachments)
        }
    }

    override fun loadManholeWithAttachmentById(id: Long): ManholeDto {
        val manholeEntity = manholeJpaRepository.findById(id).orElseThrow {
            IdNotFoundException("manhole")
        }

        val manholeAttachments = manholeAttachmentJpaRepository.findAllByManholeId(id)

        return toDto(manholeEntity, manholeAttachments)
    }

    override fun saveManhole(manholeDto: ManholeDto): ManholeEntity {
        return manholeJpaRepository.save(toEntity(manholeDto))
    }

    override fun updatedManholeWithImages(updatedManhole: ManholeDto) {
        val manholeEntity = manholeJpaRepository.findById(updatedManhole.id!!).orElseThrow {
            IdNotFoundException("manhole")
        }

        manholeEntity.status = updatedManhole.status.value
        manholeJpaRepository.save(manholeEntity)

        manholeAttachmentJpaRepository.saveAll(
            updatedManhole.afterImageUrls!!.map { imageUrl ->
                toAfterImageEntity(manholeEntity.id!!, imageUrl)
            }
        )
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
}