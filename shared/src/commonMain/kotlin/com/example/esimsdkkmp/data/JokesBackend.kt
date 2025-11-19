package com.example.esimsdkkmp.data


import com.esimsdkkmp.jokes.api.DefaultApi
import com.example.esimsdkkmp.data.repository.DadJokeRepositoryImpl
import com.example.esimsdkkmp.domain.DadJokeRepository
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import kotlinx.serialization.json.Json

object JokesBackend {

    private val defaultApi: DefaultApi by lazy {
        // 1) Platform engine (expect/actual)
        val engine = createPlatformHttpClientEngine()

        // 2) Json config for kotlinx.serialization
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

        // 3) Optional extra Ktor config (for logging, timeouts, etc.)
        val extraConfig: (HttpClientConfig<*>) -> Unit = {
            it.install(Logging) {
                level = LogLevel.BODY
                logger = Logger.SIMPLE
            }
        }
        DefaultApi(
            baseUrl = "https://icanhazdadjoke.com",
            httpClientEngine = engine,
            httpClientConfig = extraConfig,
            jsonSerializer = json
        )
    }

    val dadJokeRepository: DadJokeRepository by lazy {
        DadJokeRepositoryImpl(defaultApi)
    }
}
