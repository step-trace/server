package com.steptrace.exception

class InvalidManholeResponseTypeException(status: String) : BadRequestException(
        message = "Invalid manhole type: $status",
        displayMessage = "유효하지 않은 맨홀 응답 상태입니다: $status"
)