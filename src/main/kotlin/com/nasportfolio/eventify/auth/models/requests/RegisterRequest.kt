package com.nasportfolio.eventify.auth.models.requests

import com.nasportfolio.eventify.users.models.Role
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class RegisterRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,
    @field:NotBlank(message = "Password is required")
    @field:Length(min = 8, message = "Password must be at least 8 characters long")
    val password: String,
    @field:NotBlank(message = "Email address is required")
    @field:Email(message = "Invalid email address")
    val email: String,
    val role: Role
)