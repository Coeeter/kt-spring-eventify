package com.nasportfolio.eventify.exceptions

data class ErrorFieldResponse (
    val message: String,
    val status: Int,
    val fields: Map<String, String> = emptyMap()
)