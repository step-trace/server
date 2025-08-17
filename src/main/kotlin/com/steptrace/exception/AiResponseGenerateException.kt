package com.steptrace.exception

class AiResponseGenerateException(e: Exception) : StepTraceException(
        message = "AI 응답 생성 중 오류가 발생했습니다. ${e.printStackTrace()}",
        displayMessage = "AI 응답 생성에 실패했습니다."
)