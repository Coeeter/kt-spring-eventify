package com.nasportfolio.eventify.events.exceptions

import com.nasportfolio.eventify.exceptions.EventifyException
import org.springframework.http.HttpStatus

class EventMaxAttendeeReached(
    message: String? = "Event has reached the maximum number of attendees."
) : EventifyException(message, HttpStatus.CONFLICT)
