package com.example.esimsdkkmp.domain.model

data class CardListItem(
    val id: String,
    val title: String,
    val subtitle: String?,
    val primaryContact: String?,
    val createdAtMillis: Long,
    val imageRef: String?,
)

/**
 * Domain → UI mapper.
 */
fun BusinessCard.toCardListItem(): CardListItem =
    CardListItem(
        id = id,
        title = fullName.ifBlank { "Unknown" },
        subtitle = company,
        primaryContact = email ?: phone,
        createdAtMillis = createdAtMillis,
        imageRef = imageRef?.raw,
    )