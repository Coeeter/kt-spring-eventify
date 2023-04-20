package com.nasportfolio.eventify.users.models.requests

import javax.validation.constraints.NotBlank

data class DeleteUserRequest(
    @field:NotBlank(message = "Password is required")
    val password: String
)
