package com.nasportfolio.eventify.users.exceptions

import com.nasportfolio.eventify.exceptions.EventifyException
import org.springframework.http.HttpStatus

class InvalidCredentialsException(message: String?) : EventifyException(message, HttpStatus.UNAUTHORIZED)
