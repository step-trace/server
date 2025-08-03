package com.steptrace.common.converter

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.Convert

@Convert
class JsonStringToArrayStringConverter(override val objectMapper: ObjectMapper) :
        NullableJsonConverter<List<String>> {
    override fun getTypeReference() = object : TypeReference<List<String>>() {}
}