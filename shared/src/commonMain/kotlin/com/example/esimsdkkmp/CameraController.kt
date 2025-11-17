package com.example.esimsdkkmp

import com.example.esimsdkkmp.domain.model.BusinessCardDraft
import com.example.esimsdkkmp.domain.model.ImageRef

/**
 * Result of trying to capture an image for a business card.
 */
sealed class CaptureResult {
    data class Success(val imageRef: ImageRef) : CaptureResult()
    object Cancelled : CaptureResult()
}

/**
 * Platform-specific camera controller.
 *
 * The actual implementations live in androidMain / iosMain.
 */
expect class CameraController() {
    /**
     * Open platform-specific capture flow and return a draft,
     * or null if the user cancelled.
     */
    suspend fun captureDraft(): BusinessCardDraft?
}
