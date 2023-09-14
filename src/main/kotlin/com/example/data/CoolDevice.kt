package com.example.data

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.serde.annotation.Serdeable

@Serdeable.Serializable
data class CoolDevice(
    @JsonProperty("deviceId")
    val deviceId: String,

    @JsonProperty("status")
    val status: String,
)