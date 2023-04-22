package com.nasportfolio.eventify.categories.models

import com.nasportfolio.eventify.events.models.entities.EventEntity
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany

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
