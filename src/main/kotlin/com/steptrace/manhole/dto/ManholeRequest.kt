package com.steptrace.manhole.dto

data class ManholeRequest(
        val imageUrls: List<String>,
        val latitude: Double,
        val longitude: Double,
        val place: String,
        val title: String,
        val userDescription: String?,
        val generatedDescription: List<String>
) {
    companion object {
        private const val ALLOWED_S3_PREFIX = "https://step-trace.s3.ap-northeast-2.amazonaws.com/manholes"
    }

    init {
        require(imageUrls.isNotEmpty()) { "Image URL은 빈 값일 수 없습니다." }
        require(generatedDescription.isNotEmpty()) { "Generated description은 빈 값일 수 없습니다." }
        imageUrls.forEach { url ->
            require(url.startsWith(ALLOWED_S3_PREFIX)) { "허용되지 않은 이미지 URL입니다." }
        }
    }
}

data class AfterImageRegistrationRequest(
        val afterImageUrl: List<String>

) {
    companion object {
        private const val ALLOWED_S3_PREFIX = "https://step-trace.s3.ap-northeast-2.amazonaws.com/manholes"
    }

    init {
        afterImageUrl.forEach { url ->
            require(url.startsWith(ALLOWED_S3_PREFIX)) { "허용되지 않은 이미지 URL입니다." }
        }
    }
}

data class PushRequest(
        val latitude: Double,
        val longitude: Double,
        val token: String
)
