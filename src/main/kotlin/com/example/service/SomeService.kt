package com.example.service

import com.example.data.CoolDevice
import com.example.data.DeviceEnvironment
import jakarta.inject.Singleton
import java.util.Random

@Singleton
class SomeService : ISomeService {

    override fun getHelloWorldValue(): CoolDevice {
        return CoolDevice(deviceId = ByteArray(10) { Random().nextInt().toByte() }, status = 123L)
    }

    override fun getDeviceEnvironment(): DeviceEnvironment {
        return DeviceEnvironment(
            someSystem = "system1",
            length = "123",
            coolVersion = mapOf("key1" to "value1")
        )
    }
}