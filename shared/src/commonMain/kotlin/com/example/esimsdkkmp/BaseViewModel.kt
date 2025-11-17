package com.example.esimsdkkmp

import kotlinx.coroutines.CoroutineScope

expect open class BaseViewModel() {
    val scope: CoroutineScope

    /** Register a block to run when the VM is disposed (onCleared/deinit). */
    fun addTeardown(block: () -> Unit)
}