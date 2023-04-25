package com.nasportfolio.eventify.categories.exceptions

import com.nasportfolio.eventify.exceptions.EventifyException
import org.springframework.http.HttpStatus

class CategoryAlreadyExistsException(message: String? = "Category already exists") :
    EventifyException(message, HttpStatus.CONFLICT)