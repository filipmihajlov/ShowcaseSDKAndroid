package com.example.esimsdkkmp

import com.example.esimsdkkmp.domain.model.BusinessCardDraft

actual class CameraController actual constructor() {

    actual suspend fun captureDraft(): BusinessCardDraft? =
        BusinessCardDraft(
            fullName = "Filip (iOS fake)",
            company = "Demo Co iOS",
            email = "filip+ios@example.com",
            phone = null,
            website = "www.apple.com",
            notes = "Created by iOS CameraController (fake)",
            imageRef = null,
        )
}

