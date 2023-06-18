package com.nasportfolio.eventify.auth.models.dtos

import com.nasportfolio.eventify.auth.models.entities.PasswordResetTokenEntity
import com.nasportfolio.eventify.users.models.dtos.UserDto
import com.nasportfolio.eventify.users.models.dtos.userDto
import java.util.*

data class PasswordResetTokenDto(
    val id: String,
    val token: String,
    val expiresAt: Date,
    val resetAt: Date?,
    val disabled: Boolean,
    val user: UserDto
)

val PasswordResetTokenEntity.passwordResetTokenDto
    get() = PasswordResetTokenDto(
        id = id,
        token = token,
        expiresAt = expiresAt,
        resetAt = resetAt,
        disabled = disabled,
        user = user.userDto
    )