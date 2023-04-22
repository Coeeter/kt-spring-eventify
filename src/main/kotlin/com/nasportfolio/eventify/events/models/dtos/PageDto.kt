package com.nasportfolio.eventify.events.models.dtos

data class PageDto<T>(
    val currentPage: Int,
    val totalItems: Long,
    val totalPages: Int,
    val content: List<T>
)