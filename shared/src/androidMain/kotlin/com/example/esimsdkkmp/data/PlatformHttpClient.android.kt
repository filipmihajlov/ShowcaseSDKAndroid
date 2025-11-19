package com.example.esimsdkkmp.data

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android

actual fun createPlatformHttpClientEngine(): HttpClientEngine =
    Android.create()