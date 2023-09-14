package com.example.service

import com.example.data.CoolDevice
import jakarta.inject.Singleton

@Singleton
class SomeService {

    suspend fun getHelloWorldValue(): CoolDevice {
        return CoolDevice(deviceId = "123", status = "alive")
    }
}