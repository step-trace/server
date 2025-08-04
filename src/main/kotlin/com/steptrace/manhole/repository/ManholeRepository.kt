package com.steptrace.manhole.repository

import com.steptrace.manhole.dto.ManholeDto
import com.steptrace.manhole.dto.ManholeEntity

interface ManholeRepository {
    fun loadManholeWithAttachmentById(id: Long): ManholeDto

    fun saveManhole(manholeDto: ManholeDto): ManholeEntity

    fun saveManholeAttachments(manholeId: Long, attachments: List<String>)

    fun loadManholesWithAttachmentsBySub(sub: String): List<ManholeDto>
}