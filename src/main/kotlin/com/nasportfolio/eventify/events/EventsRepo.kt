package com.nasportfolio.eventify.events

import com.nasportfolio.eventify.events.models.entities.EventEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventsRepo : JpaRepository<EventEntity, String> {
    fun searchByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
        title: String,
        description: String,
        pageable: Pageable
    ): Page<EventEntity>
}

fun EventsRepo.searchEvents(query: String, pageable: Pageable) =
    searchByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
        query,
        query,
        pageable
    )
