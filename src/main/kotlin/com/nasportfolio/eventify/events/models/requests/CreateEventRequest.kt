package com.nasportfolio.eventify.events.models.requests

import org.hibernate.validator.constraints.URL
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.PositiveOrZero

data class CreateEventRequest(
    @field:NotBlank(message = "Title is required")
    val title: String,
    @field:NotBlank(message = "Description is required")
    val description: String,
    val startDate: Date,
    val endDate: Date,
    @field:Min(1, message = "Minimum 1 attendee per event")
    val maxAttendees: Int,
    @field:PositiveOrZero(message = "Invalid price provided")
    val price: Double,
    val categories: List<String> = emptyList(),
    @field:Valid
    val location: LocationRequest,
    @field:NotBlank(message = "Image required")
    @field:URL(message = "Invalid image given")
    val image: String
)