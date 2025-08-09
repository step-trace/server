package com.steptrace.exception

class ManholeStatusException(value: String) : BadRequestException(
    message = value,
    displayMessage = value
)