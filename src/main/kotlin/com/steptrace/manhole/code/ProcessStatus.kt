package com.steptrace.manhole.code

import com.steptrace.exception.InvalidProcessStatusException

enum class ProcessStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED;

    companion object {
        fun fromValue(status: String): ProcessStatus {
            return status.let { values().find { it.name.equals(status, ignoreCase = true) } }
                ?: throw InvalidProcessStatusException(status)
        }
    }
}