package com.nasportfolio.eventify.categories.exceptions

import com.nasportfolio.eventify.exceptions.EventifyException
import org.springframework.http.HttpStatus

class CategoryNotFoundException(message: String = "Category not found") : EventifyException(message, HttpStatus.NOT_FOUND)
