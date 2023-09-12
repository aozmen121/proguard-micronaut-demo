package com.example

import io.micronaut.runtime.Micronaut.build
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info

@OpenAPIDefinition(
    info = Info(
        title = "Demo API",
        version = "0.1"
    )
)
object Application {

    const val packageName = "com.example"

    @JvmStatic
    fun main(args: Array<String>) {
        val appContext = build()
            .args(*args)
            .packages(packageName)
            .start()
    }
}

