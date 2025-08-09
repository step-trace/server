package com.steptrace.manhole.repository

import com.steptrace.manhole.code.ProcessStatus
import com.steptrace.manhole.dto.ManholeDto
import com.steptrace.manhole.dto.ManholeEntity
import java.time.LocalDateTime

interface ManholeRepository {
    fun loadManholeById(id: Long): ManholeEntity

    fun loadManholesWithAttachment(): List<ManholeDto>

    fun loadManholeWithAttachmentById(id: Long): ManholeDto

    fun saveManhole(manholeDto: ManholeDto): ManholeEntity

    fun saveManholeAttachments(manholeId: Long, attachments: List<String>)

    fun loadManholesWithAttachmentsBySub(sub: String): List<ManholeDto>

    fun modifyManholeAfterImages(id: Long, afterImageUrls: List<String>)

    fun modifyManholeStatus(manholeEntity: ManholeEntity, status: ProcessStatus)

    fun loadPendingManholesWithAttachmentBetween(startDateTime: LocalDateTime, endDateTime: LocalDateTime): List<ManholeDto>
}