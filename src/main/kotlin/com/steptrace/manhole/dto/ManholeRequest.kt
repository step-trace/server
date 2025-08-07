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
    init {
        require(imageUrls.isNotEmpty()) { "Image URL은 빈 값일 수 없습니다." }
        require(generatedDescription.isNotEmpty()) { "Generated description은 빈 값일 수 없습니다." }
    }
}
