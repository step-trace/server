package com.steptrace.exception

import com.steptrace.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundException(e: NotFoundException): ErrorResponse {
        return ErrorResponse(errorCode = "NOT_FOUND", message = e.displayMessage)
    }

    @ExceptionHandler(BadRequestException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequestException(e: BadRequestException): ErrorResponse {
        return ErrorResponse(errorCode = "BAD_REQUEST", message = e.displayMessage)
    }

    @ExceptionHandler(ForbiddenException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleForbiddenException(e: ForbiddenException): ErrorResponse {
        return ErrorResponse(errorCode = "FORBIDDEN", message = e.displayMessage)
    }

    @ExceptionHandler(UnauthorizedException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleUnauthorizedException(e: UnauthorizedException): ErrorResponse {
        return ErrorResponse(errorCode = "UNAUTHORIZED", message = e.displayMessage)
    }

    @ExceptionHandler(RuntimeException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleRuntimeException(e: StepTraceException): ErrorResponse {
        return ErrorResponse(errorCode = "INTERNAL_SERVER_ERROR", message = e.displayMessage ?: "알 수 없는 에러가 발생했습니다.")
    }
}

data class ErrorResponse(
        val errorCode: String,
        val message: String?
)