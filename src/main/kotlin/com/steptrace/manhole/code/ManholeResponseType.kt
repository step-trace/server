package com.steptrace.manhole.code

import com.steptrace.exception.InvalidManholeResponseTypeException

enum class ManholeResponseType {
    PROCESSING, COMPLETED;

    companion object {
        fun fromValue(status: String): ManholeResponseType {
            return status.let { ManholeResponseType.values().find { it.name.equals(status, ignoreCase = true) } }
                    ?: throw InvalidManholeResponseTypeException(status)
        }
    }
}