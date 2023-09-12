package com.example.service

import jakarta.inject.Singleton

@Singleton
class SomeService {

    fun getHelloWorldValue(): String {
        return "Got Hello World"
    }
}