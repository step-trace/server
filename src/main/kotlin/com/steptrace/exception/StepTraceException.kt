package com.steptrace.exception

open class StepTraceException(
        message: String,
        val displayMessage: String? = null
) : RuntimeException(message)

open class NotFoundException(message: String, displayMessage: String?) : StepTraceException(message, displayMessage)

open class BadRequestException(message: String, displayMessage: String?) : StepTraceException(message, displayMessage)

open class ForbiddenException(message: String, displayMessage: String?) : StepTraceException(message, displayMessage)

open class UnauthorizedException(message: String, displayMessage: String?) : StepTraceException(message, displayMessage)
