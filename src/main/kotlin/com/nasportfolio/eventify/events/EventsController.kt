package com.nasportfolio.eventify.events

import com.nasportfolio.eventify.dtos.PageDto
import com.nasportfolio.eventify.events.models.entities.EventEntity
import com.nasportfolio.eventify.events.models.requests.CreateEventRequest
import com.nasportfolio.eventify.events.models.requests.FilterRequestParam
import com.nasportfolio.eventify.events.models.requests.IncludeRequestParam
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/events")
class EventsController(
    private val eventsService: EventsService
) {

    @GetMapping
    fun getAllEvents(
        @RequestParam("page") page: Int?,
        @RequestParam("size") size: Int?
    ): PageDto<EventEntity> {
        return eventsService.getAllEvents(page, size)
    }

    @GetMapping("/{id}")
    fun getEventById(@PathVariable id: String): EventEntity {
        return eventsService.getEventById(id)
    }

    @GetMapping("/search")
    fun searchEvents(
        @RequestParam query: String?,
        @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") starts: Date?,
        @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") ends: Date?,
        @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") created: Date?,
        @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") updated: Date?,
        @RequestParam minPrice: Double?,
        @RequestParam maxPrice: Double?,
        @RequestParam latitude: Double?,
        @RequestParam longitude: Double?,
        @RequestParam radius: Double?,
        @RequestParam postalCode: String?,
        @RequestParam address: String?,
        @RequestParam include: List<IncludeRequestParam>?,
        @RequestParam("category") categories: List<String>?,
        @RequestParam page: Int?,
        @RequestParam size: Int?,
    ): PageDto<EventEntity> {
        return eventsService.filterEvents(
            FilterRequestParam(
                query,
                starts,
                ends,
                created,
                updated,
                minPrice,
                maxPrice,
                latitude,
                longitude,
                radius,
                postalCode,
                address,
                include,
                categories
            ),
            page,
            size
        )
    }

    @PostMapping
    fun createEvent(
        @Valid @RequestBody request: CreateEventRequest,
        @AuthenticationPrincipal user: User
    ): EventEntity {
        return eventsService.createEvent(request, user)
    }
}