package com.steptrace.exception

class InvalidProcessStatusException(status: String) : BadRequestException(
    message = "Invalid process status: $status",
    displayMessage = "유효하지 않은 처리 상태입니다: $status"
)