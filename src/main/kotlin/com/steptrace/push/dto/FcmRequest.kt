package com.steptrace.push.dto

data class FcmRequest(
        val token: String,
        val title: String,
        val body: String
)
