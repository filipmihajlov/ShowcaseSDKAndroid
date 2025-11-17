package com.example.esimsdkkmp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

actual open class BaseViewModel {

    private val job = SupervisorJob()
    private val teardowns = mutableListOf<() -> Unit>()

    /** Call this when the VM is being released (e.g., from owner deinit). */
    fun dispose() {
        teardowns.forEach { runCatching { it() } }
        teardowns.clear()
        job.cancel()
    }

    // Safety net if the VM is GC’d without explicit dispose
    protected fun finalize() {
        dispose()
    }

    actual val scope: CoroutineScope
        get() = CoroutineScope(Dispatchers.Main + job)

    actual fun addTeardown(block: () -> Unit) {
        teardowns += block
    }
}