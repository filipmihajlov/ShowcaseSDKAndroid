package com.example.cardkit_android

import com.example.esimsdkkmp.CameraController
import com.example.esimsdkkmp.CardKit
import com.example.esimsdkkmp.ScanResult
import com.example.esimsdkkmp.domain.model.BusinessCard
import com.example.esimsdkkmp.data.repository.InMemoryBusinessCardRepository
import com.example.esimsdkkmp.domain.model.CardListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


sealed class SdkScanResult {
    data class Success(val card: CardListItem) : SdkScanResult()
    object Cancelled : SdkScanResult()
}

/**
 * Android entry point for the CardKit SDK.
 * Consumers never touch CardKit/BusinessCard directly.
 */
object CardKitAndroid {

    private val cardKit: CardKit by lazy {
        CardKit(
            repository = InMemoryBusinessCardRepository(),
            cameraController = CameraController(),
        )
    }

    fun observeCards(): Flow<List<CardListItem>> =
        cardKit.observeCards().map { list -> list.map { it.toSdkCard() } }

    suspend fun startScanFlow(): SdkScanResult =
        when (val result = cardKit.startScanFlow()) {
            is ScanResult.Success -> SdkScanResult.Success(result.card.toSdkCard())
            ScanResult.Cancelled  -> SdkScanResult.Cancelled
        }

    suspend fun deleteCard(id: String) {
        cardKit.deleteCard(id)
    }
}


private fun BusinessCard.toSdkCard() =
    CardListItem(
        id = id,
        title = fullName.ifBlank { "Unknown" },
        subtitle = company,
        primaryContact = email ?: phone,
        createdAtMillis = createdAtMillis,
        imageRef = imageRef?.raw,
    )
