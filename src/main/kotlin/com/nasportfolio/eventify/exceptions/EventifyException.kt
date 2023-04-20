package com.nasportfolio.eventify.exceptions

import org.springframework.http.HttpStatus

open class EventifyException(
    message: String?,
    val status: HttpStatus
) : RuntimeException(message)
