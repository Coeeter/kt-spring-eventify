package com.nasportfolio.eventify.exceptions

import java.util.*

data class ErrorFieldResponse(
    val timestamp: Date = Date(),
    val message: String,
    val status: Int,
    val fields: Map<String, String> = emptyMap(),
    val path: String
)