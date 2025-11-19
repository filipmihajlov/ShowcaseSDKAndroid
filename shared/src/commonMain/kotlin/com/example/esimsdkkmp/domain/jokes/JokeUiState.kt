package com.example.esimsdkkmp.domain.jokes

sealed interface JokeUiState {
    object Idle : JokeUiState
    object Loading : JokeUiState
    data class Success(val text: String) : JokeUiState
    data class Error(val message: String) : JokeUiState
}
