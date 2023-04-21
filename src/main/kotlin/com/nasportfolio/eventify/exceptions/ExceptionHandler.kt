package com.nasportfolio.eventify.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.multipart.MultipartException

@ControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(EventifyException::class)
    fun handleException(exception: EventifyException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(
                message = exception.message.toString(),
                status = exception.status.value()
            ),
            exception.status
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleException(exception: MethodArgumentNotValidException): ResponseEntity<ErrorFieldResponse> {
        return ResponseEntity(
            ErrorFieldResponse(
                message = "Invalid fields provided",
                status = HttpStatus.BAD_REQUEST.value(),
                fields = exception.bindingResult.allErrors.associate {
                    val error = it as FieldError
                    error.field to error.defaultMessage.toString()
                }
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(MultipartException::class)
    fun handleException(exception: MultipartException): ResponseEntity<ErrorFieldResponse> {
        return ResponseEntity(
            ErrorFieldResponse(
                message = "Invalid fields provided",
                status = HttpStatus.BAD_REQUEST.value(),
                fields = mapOf("image" to "Image is required")
            ),
            HttpStatus.BAD_REQUEST
        )
    }
}