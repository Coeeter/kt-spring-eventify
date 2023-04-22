package com.nasportfolio.eventify.events.exceptions

import com.nasportfolio.eventify.exceptions.EventifyException
import org.springframework.http.HttpStatus

class EventNotFoundException(message: String = "Event not found") : EventifyException(message, HttpStatus.NOT_FOUND)
