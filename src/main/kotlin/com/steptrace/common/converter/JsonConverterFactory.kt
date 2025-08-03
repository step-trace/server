package com.steptrace.common.converter


import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.AttributeConverter

interface NullableJsonConverter<T : Any> : AttributeConverter<T, String?> {

    val objectMapper: ObjectMapper

    fun getTypeReference(): TypeReference<T>

    override fun convertToDatabaseColumn(attribute: T?): String? {
        return objectMapper.writeValueAsString(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): T? {
        return dbData?.let { objectMapper.readValue(it, getTypeReference()) }
    }
}

interface NonNullableJsonConverter<T : Any> : AttributeConverter<T, String> {

    val objectMapper: ObjectMapper

    fun getTypeReference(): TypeReference<T>

    override fun convertToDatabaseColumn(attribute: T): String {
        return objectMapper.writeValueAsString(attribute)
    }

    override fun convertToEntityAttribute(dbData: String): T {
        return dbData.let { objectMapper.readValue(it, getTypeReference()) }
    }
}