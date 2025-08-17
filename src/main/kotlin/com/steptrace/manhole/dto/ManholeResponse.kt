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

data class PendingManholeResponse(
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
        fun from(dto: ManholeDto): PendingManholeResponse = with(dto) {
            PendingManholeResponse(
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

data class ReportedManholeResponse(
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
        fun from(dto: ManholeDto): ReportedManholeResponse = with(dto) {
            ReportedManholeResponse(
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

interface ManholeFromMyReportResponse {
    val id: Long
    val status: String
    val title: String
    val place: String
    val beforeImageUrls: List<String>
    val userDescription: String?
    val createdAt: LocalDateTime
}

data class PendingManholeFromMyReportResponse(
        override val id: Long,
        override val status: String,
        override val title: String,
        override val createdAt: LocalDateTime,
        override val place: String,
        override val beforeImageUrls: List<String>,
        override val userDescription: String?
): ManholeFromMyReportResponse {
    companion object {
        fun from(dto: ManholeDto): PendingManholeFromMyReportResponse = with(dto) {
            PendingManholeFromMyReportResponse(
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

data class ReportedManholeFromMyReportResponse(
        override val id: Long,
        override val status: String,
        override val title: String,
        override val createdAt: LocalDateTime,
        override val place: String,
        override val beforeImageUrls: List<String>,
        override val userDescription: String?
): ManholeFromMyReportResponse {
    companion object {
        fun from(dto: ManholeDto): ReportedManholeFromMyReportResponse = with(dto) {
            ReportedManholeFromMyReportResponse(
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

data class CompletedManholeFromMyReportResponse(
        override val id: Long,
        override val status: String,
        override val title: String,
        override val createdAt: LocalDateTime,
        override val place: String,
        override val beforeImageUrls: List<String>,
        override val userDescription: String?,
        val afterImageUrls: List<String>
): ManholeFromMyReportResponse {
    companion object {
        fun from(dto: ManholeDto): CompletedManholeFromMyReportResponse = with(dto) {
            CompletedManholeFromMyReportResponse(
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
