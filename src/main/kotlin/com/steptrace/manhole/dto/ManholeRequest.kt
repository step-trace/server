package com.steptrace.manhole.dto

data class ManholeRequest(
        val imageUrls: List<String>,
        val latitude: Double,
        val longitude: Double,
        val place: String,
        val title: String,
        val userDescription: String?,
        val generatedDescription: List<String>
)