package com.nasportfolio.eventify.events.exceptions

import com.nasportfolio.eventify.exceptions.EventifyException
import org.springframework.http.HttpStatus

class AttendeeAlreadyExistsException(
    message: String? = "User is already attending the event."
) : EventifyException(message, HttpStatus.BAD_REQUEST)