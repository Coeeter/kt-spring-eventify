package com.nasportfolio.eventify.events.exceptions

import com.nasportfolio.eventify.exceptions.EventifyException
import org.springframework.http.HttpStatus

class AttendeeIsOrganiserException(
    message: String? = "Organizers cannot attend their own events."
) : EventifyException(message, HttpStatus.BAD_REQUEST)
