package com.nasportfolio.eventify.users.models.dtos

import com.nasportfolio.eventify.users.models.UserEntity
import java.util.*

data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val createdAt: Date,
    val imageUrl: String?
)

val UserEntity.userDto
    get() = UserDto(
        id = id,
        name = name,
        email = email,
        createdAt = createdAt,
        imageUrl = imageUrl
    )