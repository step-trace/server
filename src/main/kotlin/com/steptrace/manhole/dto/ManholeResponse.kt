package com.steptrace.manhole.dto

import java.time.LocalDateTime

data class ProcessingManholeResponse(
        val id: Long,
        val latitude: Double,
        val longitude: Double,
        val status: String,
        val place: String,
        val beforeImageUrls: List<String>,
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
                    beforeImageUrls = beforeImageUrls,
                    generatedDescription = generatedDescription,
                    createdAt = createdAt!!
            )
        }
    }
}

data class CompletedManholeResponse(
        val id: Long,
        val latitude: Double,
        val longitude: Double,
        val status: String,
        val place: String,
        val beforeImageUrls: List<String>,
        val afterImageUrls: List<String>,
        val processDescription: String,
        val createdAt: LocalDateTime
) {
    companion object {
        fun from(dto: ManholeDto): CompletedManholeResponse = with(dto) {
            CompletedManholeResponse(
                    id = id!!,
                    latitude = latitude,
                    longitude = longitude,
                    status = status.value,
                    place = place,
                    beforeImageUrls = beforeImageUrls,
                    afterImageUrls = afterImageUrls!!,
                    processDescription = processDescription!!,
                    createdAt = createdAt!!
            )
        }
    }
}
