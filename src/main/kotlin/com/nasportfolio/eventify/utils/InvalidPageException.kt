package com.nasportfolio.eventify.utils

import com.nasportfolio.eventify.exceptions.EventifyException
import org.springframework.http.HttpStatus

class InvalidPageException(
    message: String = "Invalid page or size given"
) : EventifyException(message, HttpStatus.BAD_REQUEST)
