package com.nasportfolio.eventify.auth.models.requests

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message = "Email address required")
    @field:Email(message = "Invalid email address")
    val email: String,
    @field:NotBlank(message = "Password is required")
    val password: String
)