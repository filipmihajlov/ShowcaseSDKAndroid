package com.example.esimsdkkmp

import com.example.esimsdkkmp.domain.model.BusinessCardDraft

actual class CameraController actual constructor() {

//    actual suspend fun captureImageForBusinessCard(): CaptureResult {
//        // TODO: integrate with UIImagePickerController / PHPicker in the iOS app
//        // or via a delegate injected into this controller.
//        return CaptureResult.Cancelled
//        // Or simulate success:
//        // return CaptureResult.Success(ImageRef("ios-stub-image-\(currentTimeMillis())"))
//    }

    actual suspend fun captureDraft(): BusinessCardDraft? {
        TODO("Not yet implemented")
    }
}
