package com.example.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.example.serializer.ByteArray32ToBase64UrlSerialiser
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

data class CoolDevice(
    @JsonProperty("di")
    @JsonSerialize(using = ByteArray32ToBase64UrlSerialiser::class)
    val deviceId: ByteArray,

    @JsonProperty("st")
    val status: Long
)

data class DeviceEnvironment(
    @JsonProperty("some") var someSystem: String = "<undefined>",
    @JsonProperty("len") var length: String = "<undefined>",
    @JsonProperty("cool_version") var coolVersion: Map<String, String> = mapOf()
) {
    override fun toString(): String {
        return jacksonObjectMapper().writeValueAsString(this)
    }

    companion object {
        fun parse(json: String): DeviceEnvironment{
            return jacksonObjectMapper().readValue(json, DeviceEnvironment::class.java)
        }
    }
}
