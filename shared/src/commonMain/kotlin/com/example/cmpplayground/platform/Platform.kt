package com.example.cmpplayground.platform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
