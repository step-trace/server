package com.steptrace.image.dto

data class ImageS3UploadResponse(
    val fileName: String,
    val presignedUrl: String
) {
    companion object {
        fun of(fileName: String, presignedUrl: String): ImageS3UploadResponse {
            return ImageS3UploadResponse(
                    fileName = fileName,
                    presignedUrl = presignedUrl
            )
        }
    }
}