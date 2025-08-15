package com.steptrace.push.dto

data class FcmDto(
        val message: MessageDto
) {
    companion object {
        fun from(request: FcmRequest) = with(request) {
            FcmDto(
                    message = MessageDto.from(request)
            )
        }
    }
}

data class MessageDto(
        val token: String,
        val notification: NotificationDto
) {
    companion object {
        fun from(request: FcmRequest) = with(request) {
            MessageDto (
                    token = request.token,
                    notification = NotificationDto.of(request.title, request.body)
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