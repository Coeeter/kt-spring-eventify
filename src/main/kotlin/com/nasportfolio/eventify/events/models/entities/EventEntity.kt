package com.nasportfolio.eventify.events.models.entities

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
    @ManyToMany
    @JoinTable(
        name = "event_category",
        joinColumns = [JoinColumn(name = "event_id")],
        inverseJoinColumns = [JoinColumn(name = "category_id")],
    )
    val categories: List<Category> = emptyList(),
)
