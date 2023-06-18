package com.nasportfolio.eventify.events.models.dtos

import com.nasportfolio.eventify.categories.models.dtos.CategoryDto
import com.nasportfolio.eventify.categories.models.dtos.categoryDto
import com.nasportfolio.eventify.events.models.entities.EventEntity
import com.nasportfolio.eventify.users.models.dtos.UserDto
import com.nasportfolio.eventify.users.models.dtos.userDto
import java.util.*

data class EventDto(
    val id: String,
    val title: String,
    val description: String,
    val location: LocationDto,
    val startDate: Date,
    val endDate: Date,
    val price: Double,
    val image: String,
    val maxAttendees: Int,
    val organiser: UserDto,
    val createdAt: Date,
    val updatedAt: Date,
    val categories: List<CategoryDto>,
)

val EventEntity.eventDto
    get() = EventDto(
        id = id,
        title = title,
        description = description,
        location = location.locationDto,
        startDate = startDate,
        endDate = endDate,
        price = price,
        image = image,
        maxAttendees = maxAttendees,
        organiser = organiser.userDto,
        createdAt = createdAt,
        updatedAt = updatedAt,
        categories = categories.map { it.categoryDto }
    )