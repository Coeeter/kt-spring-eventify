package com.nasportfolio.eventify.events.exceptions

import com.nasportfolio.eventify.exceptions.EventifyException
import org.springframework.http.HttpStatus

class UnauthorizedEventAccessException(
    message: String? = "Only the organizer can delete this event."
): EventifyException(message, HttpStatus.UNAUTHORIZED)