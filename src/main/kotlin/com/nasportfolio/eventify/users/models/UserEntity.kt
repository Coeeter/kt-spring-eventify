package com.nasportfolio.eventify.users.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.nasportfolio.eventify.auth.models.entities.PasswordResetTokenEntity
import org.springframework.data.annotation.CreatedDate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    @Column(unique = true)
    val email: String,
    @JsonIgnore
    val password: String,
    @CreatedDate
    val createdAt: Date = Date(),
    @Enumerated(EnumType.STRING)
    val role: Role,
    @JsonIgnore
    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val passwordResetTokens: List<PasswordResetTokenEntity> = emptyList(),
    val imageUrl: String? = null,
)

enum class Role {
    ADMIN,
    ATTENDEE
}