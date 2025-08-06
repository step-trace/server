package com.steptrace.image.dto

data class S3UrlDto(
    val preSignedUrl: String,
    val key: String
)