package com.example.controller

import com.example.data.CoolDevice
import com.example.service.SomeService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import java.util.*

@Controller("/api/v1")
@ApiResponses(
    ApiResponse(responseCode = "400", description = "OHHHH NOO"),
    ApiResponse(responseCode = "404", description = "You can't see me!!"),
    ApiResponse(responseCode = "500", description = "Sorry my fault")
)
@Tag(name = "Some Controller")
class TestController(private val someService: SomeService) {

    @Get("/hello")
    @Operation(
        summary = "Hello",
        description = "Says hello-world"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            content = [Content(
                mediaType = "text/plain",
                schema = Schema(implementation = CoolDevice::class)
            )]
        ),
    )
    fun hello(): HttpResponse<CoolDevice> {
        println("Got it Working")
        return HttpResponse.ok(someService.getHelloWorldValue())
    }
}
