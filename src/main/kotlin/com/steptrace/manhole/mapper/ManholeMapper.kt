package com.steptrace.manhole.mapper

import com.steptrace.manhole.code.ProcessStatus
import com.steptrace.manhole.dto.ManholeAttachmentEntity
import com.steptrace.manhole.dto.ManholeDto
import com.steptrace.manhole.dto.ManholeEntity
import com.steptrace.manhole.dto.ManholeRequest

object ManholeMapper {

    fun toDto(request: ManholeRequest, userSub: String): ManholeDto = with(request) {
        ManholeDto(
                latitude = latitude,
                longitude = longitude,
                status = ProcessStatus.PENDING,
                title = title,
                place = place,
                userDescription = userDescription,
                userSub = userSub,
                beforeImageUrls = imageUrls,
                generatedDescription = generatedDescription
        )
    }

    fun toDto(processingManhole: ManholeEntity, processingManholeAttachments: List<ManholeAttachmentEntity>) : ManholeDto {
        return ManholeDto(
                id = processingManhole.id,
                latitude = processingManhole.latitude,
                longitude = processingManhole.longitude,
                status = ProcessStatus.fromValue(processingManhole.status),
                title = processingManhole.title,
                place = processingManhole.place,
                userDescription = processingManhole.userDescription,
                userSub = processingManhole.userSub,
                generatedDescription = processingManhole.generatedDescription,
                beforeImageUrls = processingManholeAttachments.map { it.imageUrl },
                createdBy = processingManhole.createdBy,
                createdAt = processingManhole.createdAt,
                updatedBy = processingManhole.updatedBy,
                updatedAt = processingManhole.updatedAt
        )
    }

    fun toEntity(dto: ManholeDto): ManholeEntity = with(dto) {
        ManholeEntity(
                latitude = latitude,
                longitude = longitude,
                status = status.value,
                title = title,
                place = place,
                userDescription = userDescription,
                userSub = userSub,
                generatedDescription = generatedDescription
        )
    }

    fun toEntity(manholeId: Long, imageUrl: String): ManholeAttachmentEntity {
        return ManholeAttachmentEntity(
            manholeId = manholeId,
                imageUrl = imageUrl,
                isCompleted = false
        )
    }
}