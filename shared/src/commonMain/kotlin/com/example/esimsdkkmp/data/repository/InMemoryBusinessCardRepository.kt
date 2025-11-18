package com.example.esimsdkkmp.data.repository

import com.example.esimsdkkmp.domain.model.BusinessCard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryBusinessCardRepository : BusinessCardRepository {

    private val cards = mutableListOf<BusinessCard>()
    private val state = MutableStateFlow<List<BusinessCard>>(emptyList())

    override fun observeCards(): Flow<List<BusinessCard>> = state.asStateFlow()

    override suspend fun upsert(card: BusinessCard) {
        val index = cards.indexOfFirst { it.id == card.id }
        if (index >= 0) {
            cards[index] = card
        } else {
            cards += card
        }

        state.value = cards.toList()
    }

    override suspend fun delete(id: String) {
        val removed = cards.removeAll { it.id == id }
        if (removed) {
            state.value = cards.toList()
        }
    }
}

