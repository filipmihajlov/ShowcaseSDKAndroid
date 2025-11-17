package com.example.esimsdkkmp.domain.model

import kotlin.jvm.JvmInline

@JvmInline
value class ImageRef(val raw: String)

/**
 * Immutable snapshot of a scanned / saved business card.
 */
data class BusinessCard(
    val id: String,
    val fullName: String,
    val company: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val website: String? = null,
    val notes: String? = null,
    val imageRef: ImageRef? = null,
    val createdAtMillis: Long,
)
