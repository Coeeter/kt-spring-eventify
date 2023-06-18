package com.nasportfolio.eventify.exceptions

import org.springframework.http.HttpStatus
import java.util.*

data class ErrorResponse(
    val timestamp: String = Date().toString(),
    val status: Int,
    val error: String = HttpStatus.valueOf(status).reasonPhrase,
    val message: String,
    val path: String
)