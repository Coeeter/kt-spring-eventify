package com.nasportfolio.eventify.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.multipart.MultipartException

@ControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(EventifyException::class)
    fun handleException(exception: EventifyException, request: WebRequest): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(
                message = exception.message.toString(),
                status = exception.status.value(),
                path = getRequestPath(request)
            ),
            exception.status
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleException(
        exception: MethodArgumentNotValidException,
        request: WebRequest
    ): ResponseEntity<ErrorFieldResponse> {
        return ResponseEntity(
            ErrorFieldResponse(
                message = "Invalid fields provided",
                status = HttpStatus.BAD_REQUEST.value(),
                fields = exception.bindingResult.allErrors.associate {
                    val error = it as FieldError
                    error.field to error.defaultMessage.toString()
                },
                path = getRequestPath(request)
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(MultipartException::class)
    fun handleException(exception: MultipartException, request: WebRequest): ResponseEntity<ErrorFieldResponse> {
        return ResponseEntity(
            ErrorFieldResponse(
                message = "Invalid fields provided",
                status = HttpStatus.BAD_REQUEST.value(),
                fields = mapOf("image" to "Image is required"),
                path = getRequestPath(request)
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    private fun getRequestPath(request: WebRequest): String {
        return (request as? ServletWebRequest)?.request?.requestURI ?: ""
    }
}