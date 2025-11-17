package com.example.esimsdkkmp

// shared/src/iosMain/kotlin/com/example/esimsdkkmp/photos/PhotoStore.ios.kt
actual object InMemoryPhotoStore {
    // mirror the same logic using UIImage
    actual fun put(bitmap: PlatformBitmap): String {
        TODO("Not yet implemented")
    }

    actual fun get(id: String): PlatformBitmap? {
        TODO("Not yet implemented")
    }

    actual fun remove(id: String) {
    }
}
