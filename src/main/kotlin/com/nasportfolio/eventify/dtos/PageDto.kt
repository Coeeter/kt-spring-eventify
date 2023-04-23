package com.nasportfolio.eventify.dtos

import org.springframework.data.domain.Page

data class PageDto<T> private constructor(
    val currentPage: Int,
    val totalItems: Long,
    val totalPages: Int,
    val content: List<T>
) {
    companion object {
        const val DEFAULT_SIZE = 20

        fun <T> fromPage(page: Page<T>): PageDto<T> {
            return PageDto(
                currentPage = page.number + 1,
                totalItems = page.totalElements,
                totalPages = page.totalPages,
                content = page.content
            )
        }
    }
}