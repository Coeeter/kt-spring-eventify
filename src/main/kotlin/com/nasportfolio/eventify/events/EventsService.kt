package com.nasportfolio.eventify.events

import com.nasportfolio.eventify.categories.CategoryService
import com.nasportfolio.eventify.categories.exceptions.CategoryNotFoundException
import com.nasportfolio.eventify.categories.models.requests.CategoryRequest
import com.nasportfolio.eventify.dtos.PageDto
import com.nasportfolio.eventify.dtos.PageDto.Companion.DEFAULT_SIZE
import com.nasportfolio.eventify.dtos.PageDto.Companion.fromPage
import com.nasportfolio.eventify.events.exceptions.EventMaxAttendeeReached
import com.nasportfolio.eventify.events.exceptions.EventNotFoundException
import com.nasportfolio.eventify.events.exceptions.InvalidEventException
import com.nasportfolio.eventify.events.exceptions.InvalidPageException
import com.nasportfolio.eventify.events.models.entities.EventEntity
import com.nasportfolio.eventify.events.models.entities.LocationEntity
import com.nasportfolio.eventify.events.models.requests.CreateEventRequest
import com.nasportfolio.eventify.events.models.requests.FilterRequestParam
import com.nasportfolio.eventify.users.UserService
import com.nasportfolio.eventify.users.models.UserEntity
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
            return fromPage(
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
        try {
            val pageRequest = PageRequest.of(
                (page ?: 1) - 1,
                size ?: DEFAULT_SIZE
            )
            return fromPage(
                page = eventsRepo.filterEvents(
                    filterRequestParam,
                    pageRequest
                )
            )
        } catch (e: IllegalArgumentException) {
            throw InvalidPageException("Invalid page or size given")
        }
    }

    fun getAttendees(
        id: String?,
        page: Int?,
        size: Int?
    ): PageDto<UserEntity> {
        try {
            val pageRequest = PageRequest.of(
                (page ?: 1) - 1,
                size ?: DEFAULT_SIZE
            )
            val event = eventsRepo.findByIdOrNull(id)
                ?: throw EventNotFoundException()
            return fromPage(
                page = userService.getAttendeesOfEvent(
                    event,
                    pageRequest
                )
            )
        } catch (e: IllegalArgumentException) {
            throw InvalidPageException("Invalid page or size given")
        }
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

    fun createAttendee(id: String, user: User) {
        val event = eventsRepo.findByIdOrNull(id) ?: throw EventNotFoundException()
        if (event.attendees.size == event.maxAttendees) throw EventMaxAttendeeReached()
        val userEntity = userService.getUserByEmail(user.username)
        eventsRepo.save(
            event.copy(
                attendees = event.attendees.toMutableList().apply {
                    add(userEntity)
                }
            )
        )
    }

    fun deleteAttendee(id: String, user: User) {
        val event = eventsRepo.findByIdOrNull(id) ?: throw EventNotFoundException()
        eventsRepo.save(
            event.copy(
                attendees = event.attendees.filter {
                    it.email != user.username
                }
            )
        )
    }

    fun addCategoryToEvent(
        id: String,
        categoryRequest: CategoryRequest
    ): EventEntity {
        val event = eventsRepo.findByIdOrNull(id) ?: throw EventNotFoundException()
        val category = categoryService.getCategoryByName(categoryRequest.name) ?: throw CategoryNotFoundException()
        return eventsRepo.save(
            event.copy(
                categories = setOf(*event.categories.toTypedArray(), category).toList()
            )
        )
    }

    fun deleteCategoryFromEvent(
        id: String,
        categoryName: String
    ): EventEntity {
        val event = eventsRepo.findByIdOrNull(id) ?: throw EventNotFoundException()
        val category = categoryService.getCategoryByName(categoryName) ?: throw CategoryNotFoundException()
        return eventsRepo.save(
            event.copy(
                categories = categoryService.getCategoriesByNames(
                    event.categories
                        .filter { it != category }
                        .map { it.name }
                )
            )
        )
    }
}
