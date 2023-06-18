package com.nasportfolio.eventify.categories.models

import com.nasportfolio.eventify.events.models.entities.EventEntity
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
data class Category(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    @ManyToMany(mappedBy = "categories")
    val events: List<EventEntity> = emptyList()
)
