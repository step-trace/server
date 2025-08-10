package com.steptrace.common.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Convert

@Convert
class BooleanToYNConverter : AttributeConverter<Boolean?, String?> {
    override fun convertToDatabaseColumn(attribute: Boolean?): String = when (attribute) {
        true -> "Y"
        else -> "N"
    }

    override fun convertToEntityAttribute(dbData: String?): Boolean = when (dbData) {
        "Y" -> true
        else -> false
    }
}