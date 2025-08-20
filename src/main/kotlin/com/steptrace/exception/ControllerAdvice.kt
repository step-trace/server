package com.steptrace.exception

import com.steptrace.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * 전역 예외 처리기
 * 
 * 애플리케이션 전체에서 발생하는 예외들을 통일된 형태로 처리하여
 * 클라이언트에게 일관된 오류 응답을 제공합니다.
 * HTTP 상태 코드와 에러 메시지를 예외 타입별로 매핑합니다.
 */
@RestControllerAdvice
class ControllerAdvice {

    /**
     * 404 Not Found 예외 처리
     * 
     * 요청된 리소스를 찾을 수 없을 때 발생하는 예외를 처리합니다.
     * 
     * @param e NotFoundException 인스턴스
     * @return 404 상태코드와 에러 메시지
     */
    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundException(e: NotFoundException): ErrorResponse {
        return ErrorResponse(errorCode = "NOT_FOUND", message = e.displayMessage)
    }

    /**
     * 400 Bad Request 예외 처리
     * 
     * 잘못된 요청 데이터나 비즈니스 로직 위반 시 발생하는 예외를 처리합니다.
     * 
     * @param e BadRequestException 인스턴스
     * @return 400 상태코드와 에러 메시지
     */
    @ExceptionHandler(BadRequestException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequestException(e: BadRequestException): ErrorResponse {
        return ErrorResponse(errorCode = "BAD_REQUEST", message = e.displayMessage)
    }

    /**
     * 403 Forbidden 예외 처리
     * 
     * 인증된 사용자이지만 해당 작업에 대한 권한이 없을 때 발생하는 예외를 처리합니다.
     * 
     * @param e ForbiddenException 인스턴스
     * @return 403 상태코드와 에러 메시지
     */
    @ExceptionHandler(ForbiddenException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleForbiddenException(e: ForbiddenException): ErrorResponse {
        return ErrorResponse(errorCode = "FORBIDDEN", message = e.displayMessage)
    }

    /**
     * 401 Unauthorized 예외 처리
     * 
     * 인증되지 않은 사용자가 인증이 필요한 엔드포인트에 접근할 때 발생하는 예외를 처리합니다.
     * 
     * @param e UnauthorizedException 인스턴스
     * @return 401 상태코드와 에러 메시지
     */
    @ExceptionHandler(UnauthorizedException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleUnauthorizedException(e: UnauthorizedException): ErrorResponse {
        return ErrorResponse(errorCode = "UNAUTHORIZED", message = e.displayMessage)
    }

    /**
     * 500 Internal Server Error 예외 처리
     * 
     * 애플리케이션 내부에서 발생한 예상치 못한 오류를 처리합니다.
     * StepTraceException을 상속받는 모든 커스텀 예외들을 포함합니다.
     * 
     * @param e StepTraceException 인스턴스
     * @return 500 상태코드와 에러 메시지
     */
    @ExceptionHandler(StepTraceException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleRuntimeException(e: StepTraceException): ErrorResponse {
        return ErrorResponse(errorCode = "INTERNAL_SERVER_ERROR", message = e.displayMessage ?: "알 수 없는 에러가 발생했습니다.")
    }

    /**
     * IllegalArgumentException 처리
     * 
     * 잘못된 인수가 전달되었을 때 발생하는 예외를 400 Bad Request로 처리합니다.
     * 주로 이미지 업로드 시 중복 파일 검증에서 발생합니다.
     * 
     * @param e IllegalArgumentException 인스턴스
     * @return 400 상태코드와 에러 메시지
     */
    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ErrorResponse {
        return ErrorResponse(errorCode = "BAD_REQUEST", message = e.localizedMessage)
    }
}

/**
 * 에러 응답 데이터 클래스
 * 
 * 모든 예외 상황에 대해 일관된 형태의 에러 응답을 제공합니다.
 * 
 * @property errorCode HTTP 상태코드에 대응하는 에러 코드
 * @property message 사용자에게 표시될 에러 메시지
 */
data class ErrorResponse(
        val errorCode: String,
        val message: String?
)