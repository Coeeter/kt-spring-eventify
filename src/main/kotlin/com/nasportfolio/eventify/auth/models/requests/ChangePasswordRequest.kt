package com.nasportfolio.eventify.auth.models.requests

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank

data class ChangePasswordRequest(
    @field:NotBlank(message = "Password is required")
    @field:Length(min = 8, message = "Password must be at least 8 characters long")
    val password: String
)