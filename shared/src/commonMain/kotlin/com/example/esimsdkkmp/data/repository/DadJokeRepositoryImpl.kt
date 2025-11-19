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
        val dto: HttpResponse<Joke> = api.getRandomJoke(
            accept = "application/json",
        )

        return DadJoke(
            id = dto.body().id,
            text = dto.body().joke,
            status = dto.status
        )
    }
}
