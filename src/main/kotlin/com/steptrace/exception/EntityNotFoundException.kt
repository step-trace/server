package com.steptrace.exception

class EntityNotFoundException(entityName: String) : BadRequestException(
    message = "$entityName ID not found",
    displayMessage = "$entityName ID를 찾을 수 없습니다"
)