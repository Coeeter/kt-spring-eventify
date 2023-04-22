package com.nasportfolio.eventify.events.exceptions

import com.nasportfolio.eventify.exceptions.EventifyException
import org.springframework.http.HttpStatus

class InvalidEventException(message: String?) : EventifyException(message, HttpStatus.BAD_REQUEST)
