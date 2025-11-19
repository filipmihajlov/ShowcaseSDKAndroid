package com.example.esimsdkkmp.domain

import com.example.esimsdkkmp.domain.model.DadJoke

interface DadJokeRepository {
    suspend fun getRandomJoke(): DadJoke
}