package com.nasportfolio.eventify.events

import com.nasportfolio.eventify.categories.models.requests.CategoryRequest
import com.nasportfolio.eventify.dtos.PageDto
import com.nasportfolio.eventify.events.models.dtos.EventDto
import com.nasportfolio.eventify.events.models.entities.EventEntity
import com.nasportfolio.eventify.events.models.requests.CreateEventRequest
import com.nasportfolio.eventify.events.models.requests.FilterRequestParam
import com.nasportfolio.eventify.events.models.requests.IncludeRequestParam
import com.nasportfolio.eventify.users.models.UserEntity
import com.nasportfolio.eventify.users.models.dtos.UserDto
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
    ): PageDto<EventDto> {
        return eventsService.getAllEvents(page, size)
    }

    @GetMapping("/{id}")
    fun getEventById(@PathVariable id: String): EventDto {
        return eventsService.getEventById(id)
    }

    @GetMapping("/{id}/attendees")
    fun getAttendees(
        @PathVariable id: String,
        @RequestParam page: Int?,
        @RequestParam size: Int?
    ): PageDto<UserDto> {
        return eventsService.getAttendees(id, page, size)
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
    ): PageDto<EventDto> {
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
    ): EventDto {
        return eventsService.createEvent(request, user)
    }

    @PostMapping("/{id}/categories")
    fun addCategoryToEvent(
        @PathVariable id: String,
        @RequestBody categoryRequest: CategoryRequest
    ): EventDto {
        return eventsService.addCategoryToEvent(id, categoryRequest)
    }

    @DeleteMapping("/{id}/categories/{categoryName}")
    fun deleteCategoryFromEvent(
        @PathVariable id: String,
        @PathVariable categoryName: String
    ): EventDto {
        return eventsService.deleteCategoryFromEvent(id, categoryName)
    }

    @PostMapping("/{id}/attendees")
    fun createAttendee(
        @PathVariable id: String,
        @AuthenticationPrincipal user: User
    ) {
        return eventsService.createAttendee(id, user)
    }

    @DeleteMapping("/{id}/attendees")
    fun deleteAttendee(
        @PathVariable id: String,
        @AuthenticationPrincipal user: User
    ) {
        return eventsService.deleteAttendee(id, user)
    }

    @DeleteMapping("/{id}")
    fun deleteEvent(
        @PathVariable id: String,
        @AuthenticationPrincipal user: User
    ) {
        return eventsService.deleteEvent(id, user)
    }
}