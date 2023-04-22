package com.nasportfolio.eventify.events

import com.nasportfolio.eventify.events.models.entities.EventEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventsRepo: JpaRepository<EventEntity, String> {

}
