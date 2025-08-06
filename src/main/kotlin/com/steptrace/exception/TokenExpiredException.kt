package com.steptrace.exception

class TokenExpiredException : UnauthorizedException(
    message = "Token has expired",
    displayMessage = "토큰이 만료되었습니다"
)