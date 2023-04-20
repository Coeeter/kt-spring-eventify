package com.nasportfolio.eventify.users.models.requests

import javax.validation.constraints.NotBlank

data class UpdateUserRequest(
    @field:NotBlank
    val name: String,
)
