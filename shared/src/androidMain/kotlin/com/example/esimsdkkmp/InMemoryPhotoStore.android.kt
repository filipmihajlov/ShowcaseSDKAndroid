package com.example.esimsdkkmp

import android.graphics.Bitmap
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// On Android, PlatformBitmap *is* Bitmap

actual object InMemoryPhotoStore {

    private val photos = mutableMapOf<String, PlatformBitmap>()

    @OptIn(ExperimentalUuidApi::class)
    actual fun put(bitmap: PlatformBitmap): String {
        val id = Uuid.random().toString()
        photos[id] = bitmap
        return id
    }

    actual fun get(id: String): PlatformBitmap? = photos[id]

    actual fun remove(id: String) {
        photos.remove(id)
    }
}