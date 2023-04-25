package com.nasportfolio.eventify.events.exceptions

import com.nasportfolio.eventify.exceptions.EventifyException
import org.springframework.http.HttpStatus

class EventMaxAttendeeReached(message: String? = "Max attendee count reached") :
    EventifyException(message, HttpStatus.CONFLICT)
