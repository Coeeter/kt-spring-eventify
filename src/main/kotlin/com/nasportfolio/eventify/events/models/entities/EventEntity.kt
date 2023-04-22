package com.nasportfolio.eventify.events.models.entities

import com.nasportfolio.eventify.categories.models.Category
import com.nasportfolio.eventify.users.models.UserEntity
import java.util.*
import javax.persistence.*

@Entity(name = "events")
class EventEntity(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    @Column(columnDefinition = "TEXT")
    val description: String,
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "location_id")
    val location: LocationEntity,
    val startDate: Date,
    val endDate: Date,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val maxAttendees: Int,
    val price: Double,
    val image: String,
    @ManyToMany
    @JoinTable(
        name = "event_attendees",
        joinColumns = [JoinColumn(name = "event_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")],
    )
    val attendees: List<UserEntity> = emptyList(),
    @ManyToOne
    @JoinColumn(name = "creator_id")
    val creator: UserEntity,
    @ManyToMany(mappedBy = "events")
    val categories: List<Category> = emptyList(),
)
