package com.nasportfolio.eventify.events

import com.nasportfolio.eventify.categories.CategoryService
import com.nasportfolio.eventify.categories.models.requests.CategoryRequest
import com.nasportfolio.eventify.dtos.PageDto
import com.nasportfolio.eventify.dtos.PageDto.Companion.DEFAULT_SIZE
import com.nasportfolio.eventify.dtos.PageDto.Companion.fromPage
import com.nasportfolio.eventify.events.exceptions.*
import com.nasportfolio.eventify.events.models.dtos.EventDto
import com.nasportfolio.eventify.events.models.dtos.eventDto
import com.nasportfolio.eventify.events.models.entities.EventEntity
import com.nasportfolio.eventify.events.models.entities.LocationEntity
import com.nasportfolio.eventify.events.models.requests.CreateEventRequest
import com.nasportfolio.eventify.events.models.requests.FilterRequestParam
import com.nasportfolio.eventify.users.UserService
import com.nasportfolio.eventify.users.models.dtos.UserDto
import com.nasportfolio.eventify.users.models.dtos.userDto
import com.nasportfolio.eventify.utils.EventifyPageRequest
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
    fun getAllEvents(page: Int?, size: Int?): PageDto<EventDto> {
        return fromPage(
            page = eventsRepo.findByEndDateAfter(
                pageable = EventifyPageRequest(
                    (page ?: 1) - 1,
                    size ?: DEFAULT_SIZE
                )
            ),
            transform = { it.eventDto }
        )
    }

    fun getEventById(id: String): EventDto {
        return eventsRepo.findByIdOrNull(id)?.eventDto ?: throw EventNotFoundException()
    }

    fun filterEvents(
        filterRequestParam: FilterRequestParam?,
        page: Int?,
        size: Int?
    ): PageDto<EventDto> {
        val pageRequest = EventifyPageRequest(
            (page ?: 1) - 1,
            size ?: DEFAULT_SIZE
        )
        return fromPage(
            page = eventsRepo.filterEvents(
                filterRequestParam,
                pageRequest
            ),
            transform = { it.eventDto }
        )
    }

    fun getAttendees(
        id: String,
        page: Int?,
        size: Int?
    ): PageDto<UserDto> {
        return fromPage(
            page = eventsRepo.findEventAttendeesById(
                id = id,
                pageable = EventifyPageRequest(
                    (page ?: 1) - 1,
                    size ?: DEFAULT_SIZE
                )
            ),
            transform = { it.userDto }
        )
    }

    fun createEvent(
        request: CreateEventRequest,
        securityUser: User,
    ): EventDto {
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
        return eventsRepo.save(eventEntity).eventDto
    }

    fun createAttendee(id: String, user: User) {
        val event = eventsRepo.findByIdOrNull(id) ?: throw EventNotFoundException()
        if (event.attendees.size == event.maxAttendees) throw EventMaxAttendeeReached()
        val userEntity = userService.getUserByEmail(user.username)
        if (userEntity == event.organiser) throw AttendeeIsOrganiserException()
        if (event.attendees.contains(userEntity)) throw AttendeeAlreadyExistsException()
        eventsRepo.save(
            event.copy(
                attendees = event.attendees + userEntity
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
    ): EventDto {
        val event = eventsRepo.findByIdOrNull(id) ?: throw EventNotFoundException()
        val category = categoryService.getCategoryByName(categoryRequest.name)
        return eventsRepo.save(
            event.copy(
                categories = event.categories + category
            )
        ).eventDto
    }

    fun deleteCategoryFromEvent(
        id: String,
        categoryName: String
    ): EventDto {
        val event = eventsRepo.findByIdOrNull(id) ?: throw EventNotFoundException()
        val category = categoryService.getCategoryByName(categoryName)
        return eventsRepo.save(
            event.copy(
                categories = categoryService.getCategoriesByNames(
                    event.categories
                        .filter { it != category }
                        .map { it.name }
                )
            )
        ).eventDto
    }

    fun deleteEvent(id: String, user: User) {
        val event = eventsRepo.findByIdOrNull(id) ?: throw EventNotFoundException()
        val user = userService.getUserByEmail(user.username)
        if (event.organiser != user) throw UnauthorizedEventAccessException()
        return eventsRepo.deleteById(id)
    }
}
