package com.example.cmpplayground

import com.example.cmpplayground.platform.getPlatform

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return sayHello(platform.name)
    }
}
