package com.nasportfolio.eventify.categories.models.dtos

import com.nasportfolio.eventify.categories.models.Category

data class CategoryDto(
    val id: String,
    val name: String,
)

val Category.categoryDto
    get() = CategoryDto(
        id = id,
        name = name
    )