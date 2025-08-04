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

    fun toDto(manholeEntity: ManholeEntity, manholeAttachments: List<ManholeAttachmentEntity>) : ManholeDto {
        return ManholeDto(
                id = manholeEntity.id,
                latitude = manholeEntity.latitude,
                longitude = manholeEntity.longitude,
                status = ProcessStatus.fromValue(manholeEntity.status),
                title = manholeEntity.title,
                place = manholeEntity.place,
                userDescription = manholeEntity.userDescription,
                userSub = manholeEntity.userSub,
                generatedDescription = manholeEntity.generatedDescription,
                beforeImageUrls = manholeAttachments.filterNot { it.isCompleted }.map { it.imageUrl },
                afterImageUrls = manholeAttachments.filter { it.isCompleted }.map { it.imageUrl },
                processAgency = manholeEntity.processAgency,
                processDescription = manholeEntity.processDescription,
                createdBy = manholeEntity.createdBy,
                createdAt = manholeEntity.createdAt,
                updatedBy = manholeEntity.updatedBy,
                updatedAt = manholeEntity.updatedAt
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