package com.steptrace.image.dto

data class ImageS3UploadRequest(
    val fileName: String,
    val contentType: String
)