package com.example.service

import com.example.data.CoolDevice
import com.example.data.DeviceEnvironment

interface ISomeService {
    fun getHelloWorldValue(): CoolDevice
    fun getDeviceEnvironment(): DeviceEnvironment
}