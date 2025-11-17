package com.example.esimsdkkmp

expect object InMemoryPhotoStore {
    /**
     * Store an in-memory bitmap and return its generated ID.
     */
    fun put(bitmap: PlatformBitmap): String

    /**
     * Get previously stored bitmap by ID, or null if missing.
     */
    fun get(id: String): PlatformBitmap?

    /**
     * Remove a bitmap by ID.
     */
    fun remove(id: String)
}