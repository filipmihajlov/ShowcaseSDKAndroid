package com.example.esimsdkkmp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

actual open class BaseViewModel : ViewModel() {

    private val teardowns = mutableListOf<() -> Unit>()

    actual val scope: CoroutineScope = viewModelScope

    actual fun addTeardown(block: () -> Unit) {
        teardowns += block
    }

    override fun onCleared() {
        teardowns.forEach { runCatching { it() } }
        teardowns.clear()
        super.onCleared()
    }
}
