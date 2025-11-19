package com.example.esimsdkkmp.domain

import com.esimsdkkmp.jokes.model.Joke
import com.example.esimsdkkmp.domain.model.DadJoke

interface DadJokeRepository {
    suspend fun getRandomJoke(): DadJoke
}