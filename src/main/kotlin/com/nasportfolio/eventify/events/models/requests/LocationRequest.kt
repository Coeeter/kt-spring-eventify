package com.nasportfolio.eventify.events.models.requests

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class LocationRequest(
    @field:Min(-90, message = "Invalid latitude provided")
    @field:Max(90, message = "Invalid latitude provided")
    val latitude: Double,
    @field:Min(-180, message = "Invalid longitude provided")
    @field:Max(180, message = "Invalid longitude provided")
    val longitude: Double,
    @field:Size(min = 6, max = 6, message = "Invalid postal code provided")
    val postalCode: String,
    @field:NotBlank(message = "Address is required")
    val address: String
)
