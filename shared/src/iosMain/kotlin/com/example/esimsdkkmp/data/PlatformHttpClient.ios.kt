package com.example.esimsdkkmp.data

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

actual fun createPlatformHttpClientEngine(): HttpClientEngine =
    Darwin.create()