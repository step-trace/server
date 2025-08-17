package com.steptrace.exception

import org.springframework.ai.retry.NonTransientAiException

class BadRequestToAiException(e: NonTransientAiException): BadRequestException(
        message = "전달된 요청 값에 문제가 있습니다. ${e.printStackTrace()}",
        displayMessage = "전달된 요청 값에 문제가 있습니다. ${e.message}"
)