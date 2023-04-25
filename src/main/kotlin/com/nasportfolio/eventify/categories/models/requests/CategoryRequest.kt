package com.nasportfolio.eventify.categories.models.requests

import javax.validation.constraints.NotBlank

data class CategoryRequest(
    @field:NotBlank(message = "Name is required")
    val name: String
)
