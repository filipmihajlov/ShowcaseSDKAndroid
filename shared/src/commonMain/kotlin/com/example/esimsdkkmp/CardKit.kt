package com.example.esimsdkkmp

import com.example.esimsdkkmp.domain.model.BusinessCard
import com.example.esimsdkkmp.domain.model.BusinessCardDraft
import com.example.esimsdkkmp.data.repository.BusinessCardRepository
import com.example.esimsdkkmp.timeprovider.currentTimeMillis
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

class CardKit(
    private val repository: BusinessCardRepository,
    private val cameraController: CameraController,
    private val timeProvider: () -> Long = { currentTimeMillis() },
    private val idProvider: () -> String = { randomId() },
) {

    fun observeCards(): Flow<List<BusinessCard>> = repository.observeCards()

    suspend fun createCard(draft: BusinessCardDraft): BusinessCard {
        val card = BusinessCard(
            id = idProvider(),
            fullName = draft.fullName,
            company = draft.company,
            email = draft.email,
            phone = draft.phone,
            website = draft.website,
            notes = draft.notes,
            imageRef = draft.imageRef,
            createdAtMillis = timeProvider(),
        )
        repository.upsert(card)
        return card
    }

    suspend fun deleteCard(id: String) {
        repository.delete(id)
    }

    suspend fun startScanFlow(): ScanResult {
        val draft = cameraController.captureDraft() ?: return ScanResult.Cancelled
        val card = createCard(draft)
        return ScanResult.Success(card)
    }
}

sealed class ScanResult {
    data class Success(val card: BusinessCard) : ScanResult()
    object Cancelled : ScanResult()
}

internal fun randomId(): String =
    buildString {
        val chars = ('a'..'f') + ('0'..'9')
        repeat(16) {
            append(chars.random(Random))
        }
    }

