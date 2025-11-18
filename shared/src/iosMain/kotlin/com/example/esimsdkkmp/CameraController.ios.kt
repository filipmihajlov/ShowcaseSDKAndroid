package com.example.esimsdkkmp

import com.example.esimsdkkmp.domain.model.BusinessCardDraft

actual class CameraController actual constructor() {

    actual suspend fun captureDraft(): BusinessCardDraft? =
        BusinessCardDraft(
            fullName = "Filip (iOS fake)",
            company = "Demo Co iOS",
            email = "filip+ios@example.com",
            phone = null,
            website = null,
            notes = "Created by iOS CameraController (fake)",
            imageRef = null,
        )
}

