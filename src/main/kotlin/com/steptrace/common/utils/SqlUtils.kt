package com.steptrace.common.utils

object SqlUtils {

    fun booleanToYN(value: Boolean): String = when (value) {
        true -> "Y"
        else -> "N"
    }

    fun ynToBoolean(value: String): Boolean = when (value.uppercase()) {
        "Y" -> true
        else -> false
    }
}