package com.nasportfolio.eventify.events.models.requests

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.format.annotation.DateTimeFormat
import java.util.*

data class FilterRequestParam(
    val query: String?,
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    val starts: Date?,
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    val ends: Date?,
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    val created: Date?,
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    val updated: Date?,
    val minPrice: Double?,
    val maxPrice: Double?,
    val latitude: Double?,
    val longitude: Double?,
    val radius: Double?,
    val postalCode: String?,
    val address: String?,
    val include: List<IncludeRequestParam>?,
    @JsonProperty("category")
    val categories: List<String>?,
)