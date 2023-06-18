package com.nasportfolio.eventify.events.models.dtos

import com.nasportfolio.eventify.events.models.entities.LocationEntity

data class LocationDto(
    val latitude: Double,
    val longitude: Double,
    val postalCode: String,
    val address: String,
)

val LocationEntity.locationDto
    get() = LocationDto(
        latitude = latitude,
        longitude = longitude,
        postalCode = postalCode,
        address = address,
    )