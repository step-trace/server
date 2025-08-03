package com.steptrace.manhole.code

enum class ProcessStatus(val value: String) {
    PENDING("접수 전"),
    IN_PROGRESS("처리 중"),
    COMPLETED("처리 완료");

    companion object {
        fun fromValue(status: String): ProcessStatus {
            return status.let { values().find { it.value.equals(status, ignoreCase = true) } }
                ?: throw IllegalArgumentException("Unknown ProcessStatus value: $status") // todo: 커스텀 예외로 변경
        }
    }
}