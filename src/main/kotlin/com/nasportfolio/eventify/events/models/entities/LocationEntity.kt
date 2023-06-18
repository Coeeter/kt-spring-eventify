package com.nasportfolio.eventify.events.models.entities

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity(name = "locations")
data class LocationEntity(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val latitude: Double,
    val longitude: Double,
    val postalCode: String,
    val address: String,
    @OneToOne(mappedBy = "location")
    val event: EventEntity? = null
)
