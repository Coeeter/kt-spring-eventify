package com.nasportfolio.eventify.exceptions

import java.util.Date

data class ErrorResponse(
    val timestamp: Date = Date(),
    val message: String,
    val status: Int,
    val path: String
)