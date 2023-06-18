package com.nasportfolio.eventify.auth.models.dtos

import com.nasportfolio.eventify.auth.models.entities.PasswordResetTokenEntity
import java.util.*

data class PasswordResetTokenDto(
    val token: String,
    val expiresAt: Date,
    val disabled: Boolean
)

val PasswordResetTokenEntity.passwordResetTokenDto
    get() = PasswordResetTokenDto(
        token = token,
        expiresAt = expiresAt,
        disabled = disabled || resetAt != null || expiresAt.time < System.currentTimeMillis(),
    )