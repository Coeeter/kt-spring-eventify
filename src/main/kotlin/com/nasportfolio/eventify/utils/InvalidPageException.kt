package com.nasportfolio.eventify.utils

import com.nasportfolio.eventify.exceptions.EventifyException
import org.springframework.http.HttpStatus

class InvalidPageException(message: String?) : EventifyException(message, HttpStatus.BAD_REQUEST)
