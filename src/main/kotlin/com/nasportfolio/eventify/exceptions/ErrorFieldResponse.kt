package com.nasportfolio.eventify.exceptions

import org.springframework.http.HttpStatus
import java.util.*

data class ErrorFieldResponse(
    val timestamp: String = Date().toString(),
    val status: Int,
    val error: String = HttpStatus.valueOf(status).reasonPhrase,
    val message: String,
    val fields: Map<String, String> = emptyMap(),
    val path: String
)