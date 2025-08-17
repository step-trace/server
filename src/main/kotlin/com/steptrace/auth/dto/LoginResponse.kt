package com.steptrace.auth.dto

data class LoginResponse(
        val name: String,
        val email: String,
        val picture: String,
        val jwt: String,
) {
    companion object {
        fun of(token: String, userInfoDto: UserInfoDto): LoginResponse {
            return LoginResponse(
                    name = userInfoDto.name ?: userInfoDto.nickname!!,
                    email = userInfoDto.email,
                    picture = userInfoDto.picture,
                    jwt = token,
            )
        }
    }
}
