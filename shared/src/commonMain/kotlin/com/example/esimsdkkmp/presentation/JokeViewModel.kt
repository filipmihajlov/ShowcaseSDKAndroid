package com.example.esimsdkkmp.presentation

import com.example.esimsdkkmp.BaseViewModel
import com.example.esimsdkkmp.data.JokesBackend
import com.example.esimsdkkmp.domain.DadJokeRepository
import com.example.esimsdkkmp.domain.jokes.JokeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JokeViewModel(
    private val repo: DadJokeRepository = JokesBackend.dadJokeRepository
) : BaseViewModel() {

    private val _state = MutableStateFlow<JokeUiState>(JokeUiState.Idle)
    val state: StateFlow<JokeUiState> = _state

    fun loadJoke() {
        _state.value = JokeUiState.Loading

        scope.launch {
            try {
                val joke = repo.getRandomJoke()
                _state.value = JokeUiState.Success(joke.text)
            } catch (t: Throwable) {
                _state.value = JokeUiState.Error(t.message ?: "Unknown error")
            }
        }
    }
}