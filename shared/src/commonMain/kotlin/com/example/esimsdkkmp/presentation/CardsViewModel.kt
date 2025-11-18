package com.example.esimsdkkmp.presentation

import com.example.esimsdkkmp.domain.model.PhotoCard
import com.example.esimsdkkmp.domain.model.toCardListItem
import com.example.esimsdkkmp.BaseViewModel
import com.example.esimsdkkmp.CardKit
import com.example.esimsdkkmp.InMemoryPhotoStore
import com.example.esimsdkkmp.ScanResult
import com.example.esimsdkkmp.timeprovider.currentTimeMillis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CardsViewModel(
    private val cardKit: CardKit,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(CardsUiState())
    val uiState: StateFlow<CardsUiState> = _uiState.asStateFlow()

    init {
        scope.launch {
            cardKit.observeCards().collect { cards ->
                _uiState.update { state ->
                    state.copy(
                        cards = cards.map { it.toCardListItem() }
                    )
                }
            }
        }
    }

    fun onPhotoCaptured(photoId: String) {
        _uiState.update { state ->
            state.copy(
                photoCards = state.photoCards + PhotoCard(
                    id = photoId,
                    createdAtMillis = currentTimeMillis()
                )
            )
        }
    }

    fun onDeletePhotoClicked(id: String) {
        _uiState.update { state ->
            state.copy(
                photoCards = state.photoCards.filterNot { it.id == id }
            )
        }
        InMemoryPhotoStore.remove(id)
    }

    fun onScanClicked() {
        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                when (cardKit.startScanFlow()) {
                    is ScanResult.Success -> { /* list updates via Flow */ }
                    ScanResult.Cancelled   -> { /* no-op for now */ }
                }
            } catch (t: Throwable) {
                _uiState.update { it.copy(error = t.message ?: "Scan failed") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onDeleteCardClicked(id: String) {
        scope.launch {
            try {
                cardKit.deleteCard(id)
                // observeCards() will emit new list
            } catch (t: Throwable) {
                _uiState.update { it.copy(error = t.message ?: "Delete failed") }
            }
        }
    }
}
