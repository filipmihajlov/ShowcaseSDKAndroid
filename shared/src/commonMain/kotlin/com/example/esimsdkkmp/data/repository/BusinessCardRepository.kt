package com.example.esimsdkkmp.data.repository

import com.example.esimsdkkmp.domain.model.BusinessCard
import kotlinx.coroutines.flow.Flow

interface BusinessCardRepository {

    fun observeCards(): Flow<List<BusinessCard>>

    suspend fun upsert(card: BusinessCard)

    suspend fun delete(id: String)
}

