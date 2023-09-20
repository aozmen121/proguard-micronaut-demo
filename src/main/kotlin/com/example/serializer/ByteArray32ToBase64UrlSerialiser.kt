package com.example.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kotlin.IllegalArgumentException

class ByteArray32ToBase64UrlSerialiser : JsonSerializer<ByteArray>() {

    override fun serialize(value: ByteArray, gen: JsonGenerator, provider: SerializerProvider?) {
        if(value.isEmpty()) throw IllegalArgumentException("Cannot serialise empty value")
        gen.writeString(value.toString())
    }

}
