package com.steptrace.manhole.dto

import com.steptrace.manhole.code.ProcessStatus
import java.time.LocalDateTime

data class ManholeDto(
        val id: Long? = null,
        val latitude: Double,
        val longitude: Double,
        val status: ProcessStatus,
        val title: String,
        val place: String,
        val generatedDescription: List<String>,
        val userDescription: String? = null,
        val userSub: String,
        val beforeImageUrls: List<String>,
        val afterImageUrls: List<String>? = emptyList(),
        val createdBy: String? = null,
        val createdAt: LocalDateTime? = null,
        val updatedBy: String? = null,
        val updatedAt: LocalDateTime? = null
)