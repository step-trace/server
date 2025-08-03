package com.steptrace.manhole.repository

import com.steptrace.manhole.dto.ManholeAttachmentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ManholeAttachmentJpaRepository: JpaRepository<ManholeAttachmentEntity, Long> {
    fun findAllByManholeId(id: Long): List<ManholeAttachmentEntity>
}