package com.nasportfolio.eventify.categories.models

import com.nasportfolio.eventify.events.models.entities.EventEntity
import java.util.*
import javax.persistence.*

@Entity
data class Category(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    @ManyToMany
    @JoinTable(
        name = "event_category",
        joinColumns = [JoinColumn(name = "category_id")],
        inverseJoinColumns = [JoinColumn(name = "event_id")]
    )
    val events: List<EventEntity> = emptyList()
)
