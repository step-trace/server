package com.steptrace.exception

class UserNotFoundException : BadRequestException(
    message = "User not found",
    displayMessage = "사용자를 찾을 수 없습니다"
)