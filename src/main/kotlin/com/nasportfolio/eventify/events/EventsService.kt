package com.nasportfolio.eventify.events

import com.nasportfolio.eventify.categories.CategoryService
import com.nasportfolio.eventify.dtos.PageDto
import com.nasportfolio.eventify.dtos.PageDto.Companion.DEFAULT_SIZE
import com.nasportfolio.eventify.events.exceptions.EventNotFoundException
import com.nasportfolio.eventify.events.exceptions.InvalidEventException
import com.nasportfolio.eventify.events.exceptions.InvalidPageException
import com.nasportfolio.eventify.events.models.entities.EventEntity
import com.nasportfolio.eventify.events.models.entities.LocationEntity
import com.nasportfolio.eventify.events.models.requests.CreateEventRequest
import com.nasportfolio.eventify.events.models.requests.FilterRequestParam
import com.nasportfolio.eventify.users.UserService
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Service
import java.util.*

@Service
class EventsService(
    private val eventsRepo: EventsRepo,
    private val userService: UserService,
    private val categoryService: CategoryService,
) {
    fun getAllEvents(page: Int?, size: Int?): PageDto<EventEntity> {
        try {
            return PageDto.fromPage(
                eventsRepo.findByEndDateAfter(
                    pageable = PageRequest.of(
                        (page ?: 1) - 1,
                        size ?: DEFAULT_SIZE
                    )
                )
            )
        } catch (e: IllegalArgumentException) {
            throw InvalidPageException("Invalid page or size given")
        }
    }

    fun getEventById(id: String): EventEntity {
        return eventsRepo.findByIdOrNull(id) ?: throw EventNotFoundException()
    }

    fun filterEvents(
        filterRequestParam: FilterRequestParam?,
        page: Int?,
        size: Int?
    ): PageDto<EventEntity> {
        val pageRequest = PageRequest.of(
            (page ?: 1) - 1,
            size ?: DEFAULT_SIZE
        )
        return PageDto.fromPage(
            page = eventsRepo.filterEvents(
                filterRequestParam,
                pageRequest
            )
        )
    }

    fun createEvent(
        request: CreateEventRequest,
        securityUser: User,
    ): EventEntity {
        val user = userService.getUserByEmail(securityUser.username)
        if (request.startDate.after(request.endDate)) {
            throw InvalidEventException("Start date should not be after end date")
        }
        val eventEntity = EventEntity(
            title = request.title,
            description = request.description,
            startDate = request.startDate,
            endDate = request.endDate,
            createdAt = Date(),
            updatedAt = Date(),
            organiser = user,
            price = request.price,
            maxAttendees = request.maxAttendees,
            image = request.image,
            categories = categoryService.getCategoriesByNames(request.categories),
            location = LocationEntity(
                latitude = request.location.latitude,
                longitude = request.location.longitude,
                address = request.location.address,
                postalCode = request.location.postalCode
            ),
        )
        return eventsRepo.save(eventEntity)
    }
}
