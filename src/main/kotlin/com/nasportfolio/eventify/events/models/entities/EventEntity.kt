package com.nasportfolio.eventify.events.models.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.nasportfolio.eventify.categories.models.Category
import com.nasportfolio.eventify.users.models.UserEntity
import java.util.*
import javax.persistence.*

@Entity(name = "events")
data class EventEntity(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    @Column(columnDefinition = "TEXT")
    val description: String,
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "location_id")
    val location: LocationEntity,
    val startDate: Date,
    val endDate: Date,
    val price: Double,
    val image: String,
    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "event_attendees",
        joinColumns = [JoinColumn(name = "event_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")],
    )
    val attendees: List<UserEntity> = emptyList(),
    val maxAttendees: Int,
    @ManyToOne
    @JoinColumn(name = "creator_id")
    val organiser: UserEntity,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    @ManyToMany(mappedBy = "events")
    val categories: List<Category> = emptyList(),
)
