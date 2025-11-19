package com.example.esimsdkkmp.data.repository

import com.esimsdkkmp.jokes.api.DefaultApi
import com.esimsdkkmp.jokes.infrastructure.HttpResponse
import com.esimsdkkmp.jokes.model.Joke
import com.example.esimsdkkmp.domain.DadJokeRepository
import com.example.esimsdkkmp.domain.model.DadJoke

class DadJokeRepositoryImpl(
    private val api: DefaultApi
) : DadJokeRepository {

    override suspend fun getRandomJoke(): DadJoke {
        // 1) Call the generated API, get the wrapper
        val response: HttpResponse<Joke> = api.getRandomJoke(
            accept = "application/json",
//            userAgent = "esimsdkkmp-demo"
        )

        // 2) Unwrap the Joke from the response
        //    One of these will exist depending on the template version:
        return DadJoke(
            id = response.body().id,
            text = response.body().joke,
            status = response.body().status,
        )   // or response.data / response.payload
    }
}
