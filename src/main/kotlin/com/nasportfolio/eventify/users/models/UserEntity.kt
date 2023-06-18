package com.nasportfolio.eventify.users.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.nasportfolio.eventify.auth.models.entities.PasswordResetTokenEntity
import com.nasportfolio.eventify.events.models.entities.EventEntity
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
    val password: String,
    @CreatedDate
    val createdAt: Date = Date(),
    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val passwordResetTokens: List<PasswordResetTokenEntity> = emptyList(),
    val imageUrl: String? = null,
    @ManyToMany(mappedBy = "attendees")
    val attendingEvents: List<EventEntity> = emptyList(),
    @OneToMany(
        mappedBy = "organiser",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    val createdEvents: List<EventEntity> = emptyList()
)