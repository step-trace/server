package com.steptrace.exception

class ExcelGenerationException : StepTraceException(
    message = "Excel 파일 생성 중 오류가 발생했습니다.",
    displayMessage = "Excel 파일 생성에 실패했습니다."
)