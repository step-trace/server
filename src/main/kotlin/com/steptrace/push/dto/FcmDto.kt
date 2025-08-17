package com.steptrace.push.dto

data class FcmDto(
        val message: MessageDto
) {
    companion object {
        fun from(token: String): FcmDto{
            return FcmDto(
                    message = MessageDto.of(
                            token,
                            "경고\uFE0F\uD83D\uDEA8",
                            "발밑 조심! 반경 100m 내에 위험 맨홀이 있어요."
                    )
            )
        }
    }
}

data class MessageDto(
        val token: String,
        val notification: NotificationDto
) {
    companion object {
        fun of(token: String, title: String, body: String): MessageDto {
            return MessageDto(
                    token = token,
                    notification = NotificationDto.of(title, body)
            )
        }
    }
}

data class NotificationDto(
        val title: String,
        val body: String
) {
    companion object {
        fun of(title: String, body: String): NotificationDto {
            return NotificationDto(
                    title = title,
                    body = body
            )
        }
    }
}