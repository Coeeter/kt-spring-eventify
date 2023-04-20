package com.nasportfolio.eventify.auth.models.entities

import com.nasportfolio.eventify.users.models.UserEntity
import java.security.SecureRandom
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "password_reset")
data class PasswordResetTokenEntity(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val token: String = Base64.getUrlEncoder()
        .withoutPadding()
        .encodeToString(
            ByteArray(20).also {
                SecureRandom().nextBytes(it)
            }
        ),
    val expiresAt: Date,
    val resetAt: Date? = null,
    val disabled: Boolean = false,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: UserEntity
)