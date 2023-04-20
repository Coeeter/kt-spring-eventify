package com.nasportfolio.eventify.users.exceptions

import com.nasportfolio.eventify.exceptions.EventifyException
import org.springframework.http.HttpStatus

class UserNotFoundException(message: String) : EventifyException(message, HttpStatus.NOT_FOUND)