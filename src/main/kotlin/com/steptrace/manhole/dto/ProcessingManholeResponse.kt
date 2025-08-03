package com.steptrace.manhole.dto

import java.time.LocalDateTime

data class ProcessingManholeResponse(
        val id: Long,
        val latitude: Double,
        val longitude: Double,
        val status: String,
        val place: String,
        val imageUrls: List<String>,
        val generatedDescription: List<String>,
        val createdAt: LocalDateTime
) {
    companion object{
        fun from(dto: ManholeDto): ProcessingManholeResponse = with(dto) {
            ProcessingManholeResponse(
                id = id!!,
                latitude = latitude,
                longitude = longitude,
                status = status.value,
                place = place,
                imageUrls = beforeImageUrls,
                generatedDescription = generatedDescription,
                createdAt = createdAt!!
            )
        }
    }
}
