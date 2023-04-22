package com.nasportfolio.eventify.users.exceptions

import com.nasportfolio.eventify.exceptions.EventifyException
import org.springframework.http.HttpStatus

class InvalidUpdateUserFieldsException(message: String?) : EventifyException(message, HttpStatus.BAD_REQUEST)