package com.steptrace.manhole.dto

import java.time.LocalDateTime

data class ManholeMakerResponse(
        val id: Long,
        val latitude: Double,
        val longitude: Double,
        val status: String,
) {
    companion object {
        fun from(dto: ManholeDto): ManholeMakerResponse = with(dto) {
            ManholeMakerResponse(
                    id = id!!,
                    latitude = latitude,
                    longitude = longitude,
                    status = status.name
            )
        }
    }
}

interface ManholeResponse {
    val id: Long
    val latitude: Double
    val longitude: Double
    val status: String
    val place: String
    val beforeImageUrls: List<String>
    val createdAt: LocalDateTime
}

data class ProcessingManholeResponse(
        override val id: Long,
        override val latitude: Double,
        override val longitude: Double,
        override val status: String,
        override val place: String,
        override val beforeImageUrls: List<String>,
        override val createdAt: LocalDateTime,
        val generatedDescription: List<String>
): ManholeResponse {
    companion object {
        fun from(dto: ManholeDto): ProcessingManholeResponse = with(dto) {
            ProcessingManholeResponse(
                    id = id!!,
                    latitude = latitude,
                    longitude = longitude,
                    status = status.name,
                    place = place,
                    beforeImageUrls = beforeImageUrls,
                    generatedDescription = generatedDescription,
                    createdAt = createdAt!!
            )
        }
    }
}

data class CompletedManholeResponse(
        override val id: Long,
        override val latitude: Double,
        override val longitude: Double,
        override val status: String,
        override val place: String,
        override val beforeImageUrls: List<String>,
        override val createdAt: LocalDateTime,
        val afterImageUrls: List<String>
): ManholeResponse {
    companion object {
        fun from(dto: ManholeDto): CompletedManholeResponse = with(dto) {
            CompletedManholeResponse(
                    id = id!!,
                    latitude = latitude,
                    longitude = longitude,
                    status = status.name,
                    place = place,
                    beforeImageUrls = beforeImageUrls,
                    afterImageUrls = afterImageUrls!!,
                    createdAt = createdAt!!
            )
        }
    }
}

data class ManholesFromMyReportResponse(
        val id: Long,
        val status: String,
        val title: String,
        val createdAt: LocalDateTime,
        val imageUrl: String
) {
    companion object {
        fun from(dto: ManholeDto): ManholesFromMyReportResponse = with(dto) {
            ManholesFromMyReportResponse(
                    id = id!!,
                    status = status.name,
                    title = title,
                    createdAt = createdAt!!,
                    imageUrl = afterImageUrls?.firstOrNull() ?: beforeImageUrls.first()
            )
        }
    }
}

data class ProcessingManholesFromMyReportResponse(
        val id: Long,
        val status: String,
        val title: String,
        val createdAt: LocalDateTime,
        val place: String,
        val beforeImageUrls: List<String>,
        val userDescription: String?
) {
    companion object {
        fun from(dto: ManholeDto): ProcessingManholesFromMyReportResponse = with(dto) {
            ProcessingManholesFromMyReportResponse(
                    id = id!!,
                    status = status.name,
                    title = title,
                    createdAt = createdAt!!,
                    place = place,
                    beforeImageUrls = beforeImageUrls,
                    userDescription = userDescription
            )
        }
    }
}

data class CompletedManholesFromMyReportResponse(
        val id: Long,
        val status: String,
        val title: String,
        val createdAt: LocalDateTime,
        val place: String,
        val beforeImageUrls: List<String>,
        val afterImageUrls: List<String>,
        val userDescription: String?
) {
    companion object {
        fun from(dto: ManholeDto): CompletedManholesFromMyReportResponse = with(dto) {
            CompletedManholesFromMyReportResponse(
                    id = id!!,
                    status = status.name,
                    title = title,
                    createdAt = createdAt!!,
                    place = place,
                    beforeImageUrls = beforeImageUrls,
                    afterImageUrls = afterImageUrls!!,
                    userDescription = userDescription,
            )
        }
    }
}
