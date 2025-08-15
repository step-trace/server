package com.steptrace.exception

class AiResponseGenerateException : StepTraceException(
        message = "AI 응답 생성 중 오류가 발생했습니다.",
        displayMessage = "AI 응답 생성에 실패했습니다."
)