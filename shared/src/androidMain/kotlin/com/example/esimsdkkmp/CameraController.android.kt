package com.example.esimsdkkmp

import com.example.esimsdkkmp.domain.model.BusinessCardDraft

actual class CameraController actual constructor() {

    companion object {
        /**
         * Hook that the Android facade (or host app later) can override.
         * For now we keep a simple fake implementation.
         */
        var launcher: suspend () -> BusinessCardDraft? = {
            BusinessCardDraft(
                fullName = "Filip (Android fake)",
                company = "Demo Co",
                email = "filip+android@example.com",
                phone = null,
                website = null,
                notes = "Created by Android CameraController",
                imageRef = null,
            )
        }
    }

    actual suspend fun captureDraft(): BusinessCardDraft? = launcher()
}
