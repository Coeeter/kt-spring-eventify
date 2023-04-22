package com.nasportfolio.eventify.events

import com.nasportfolio.eventify.events.models.entities.EventEntity
import com.nasportfolio.eventify.events.models.requests.CreateEventRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/events")
class EventsController(
    private val eventsService: EventsService
) {

    @GetMapping
    fun getAllEvents(): List<EventEntity> {
        return eventsService.getAllEvents()
    }

    @GetMapping("/{id}")
    fun getEventById(@PathVariable id: String): EventEntity {
        return eventsService.getEventById(id)
    }

    @PostMapping
    fun createEvent(
        @Valid @RequestBody request: CreateEventRequest,
        @AuthenticationPrincipal user: User
    ): EventEntity {
        return eventsService.createEvent(request, user)
    }
}