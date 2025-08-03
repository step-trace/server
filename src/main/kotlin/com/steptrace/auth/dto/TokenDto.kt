package com.steptrace.auth.dto

interface TokenDto {
    val accessToken: String
    val expiresIn: Long
    val tokenType: String
    val idToken: String?
}