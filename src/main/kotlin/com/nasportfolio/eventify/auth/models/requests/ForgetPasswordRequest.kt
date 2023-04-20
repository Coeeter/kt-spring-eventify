package com.nasportfolio.eventify.auth.models.requests

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class ForgetPasswordRequest(
    @field:NotBlank(message = "Email address is required")
    @field:Email(message = "Invalid email address")
    val email: String
)