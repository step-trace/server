package com.steptrace.image.dto

data class ImageS3UploadResponse(
        val fileName: String,
        val contentType: String,
        val presignedUrl: String
) {
    companion object {
        fun of(fileName: String, contentType: String, presignedUrl: String): ImageS3UploadResponse {
            return ImageS3UploadResponse(
                    fileName = fileName,
                    contentType = contentType,
                    presignedUrl = presignedUrl
            )
        }
    }
}