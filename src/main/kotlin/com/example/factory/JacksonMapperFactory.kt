package com.example.factory

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class JacksonMapperFactory {

    @Singleton
    fun createObjectMapper(): ObjectMapper {
        return ObjectMapper().findAndRegisterModules()
    }
}