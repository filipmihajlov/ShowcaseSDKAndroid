package com.example.esimsdkkmp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

interface Closeable {
    fun close()
}

/**
 * Observe a StateFlow from Swift.
 *
 * - Calls [block] immediately with the current value
 * - Then keeps collecting until [Closeable.close] is called
 */
fun <T> watch(stateFlow: StateFlow<T>, block: (T) -> Unit): Closeable {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    // Emit current value immediately
    block(stateFlow.value)

    // Then collect updates
    val job = scope.launch {
        stateFlow.collect { value -> block(value) }
    }

    return object : Closeable {
        override fun close() {
            job.cancel()
            scope.cancel()
        }
    }
}
