package com.example.esimsdkkmp.domain.model

/**
 * Draft coming from OCR or manual input.
 * SDK will turn this into a BusinessCard with id + timestamps.
 */
data class BusinessCardDraft(
    val fullName: String,
    val company: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val website: String? = null,
    val notes: String? = null,
    val imageRef: ImageRef? = null,
)
