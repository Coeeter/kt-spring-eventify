package com.nasportfolio.eventify.categories.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.nasportfolio.eventify.events.models.entities.EventEntity
import java.util.*
import javax.persistence.*

@Entity
data class Category(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    @JsonIgnore
    @ManyToMany(mappedBy = "categories")
    val events: List<EventEntity> = emptyList()
)
