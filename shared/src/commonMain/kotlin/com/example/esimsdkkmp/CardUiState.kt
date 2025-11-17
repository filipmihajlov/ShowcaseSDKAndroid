package com.example.esimsdkkmp

import com.example.esimsdkkmp.domain.model.CardListItem
import com.example.esimsdkkmp.domain.model.PhotoCard

data class CardsUiState(
    val cards: List<CardListItem> = emptyList(),
    val photoCards: List<PhotoCard> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)