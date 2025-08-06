package com.steptrace.exception

class InvalidTokenException : BadRequestException(
    message = "Invalid token",
    displayMessage = "유효하지 않은 토큰입니다"
)