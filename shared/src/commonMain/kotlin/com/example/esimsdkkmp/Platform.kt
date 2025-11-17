package com.example.esimsdkkmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform