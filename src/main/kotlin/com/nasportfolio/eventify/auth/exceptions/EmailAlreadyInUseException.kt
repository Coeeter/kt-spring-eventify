package com.nasportfolio.eventify.auth.exceptions

import com.nasportfolio.eventify.exceptions.EventifyException
import org.springframework.http.HttpStatus

class EmailAlreadyInUseException(message: String?) : EventifyException(message, HttpStatus.CONFLICT)