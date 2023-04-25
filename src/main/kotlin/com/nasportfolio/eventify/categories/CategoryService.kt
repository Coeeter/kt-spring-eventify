package com.nasportfolio.eventify.categories

import com.nasportfolio.eventify.categories.exceptions.CategoryAlreadyExistsException
import com.nasportfolio.eventify.categories.exceptions.CategoryNotFoundException
import com.nasportfolio.eventify.categories.models.Category
import com.nasportfolio.eventify.categories.models.requests.CategoryRequest
import com.nasportfolio.eventify.dtos.PageDto
import com.nasportfolio.eventify.dtos.PageDto.Companion.DEFAULT_SIZE
import com.nasportfolio.eventify.dtos.PageDto.Companion.fromPage
import com.nasportfolio.eventify.events.EventsRepo
import com.nasportfolio.eventify.events.exceptions.InvalidPageException
import com.nasportfolio.eventify.events.models.entities.EventEntity
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryRepo: CategoryRepo,
    private val eventsRepo: EventsRepo
) {
    fun getCategoriesByNames(categories: List<String>): List<Category> {
        return categoryRepo.findByNameIn(categories)
    }

    fun getCategories(name: String?, page: Int?, size: Int?): PageDto<Category> {
        try {
            return PageDto.fromPage(
                page = categoryRepo.findByNameContainingIgnoreCase(
                    name = name ?: "",
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

    fun createCategory(request: CategoryRequest): Category {
        categoryRepo.findByName(request.name.lowercase())?.let {
            throw CategoryAlreadyExistsException()
        }
        return categoryRepo.save(
            Category(name = request.name.lowercase())
        )
    }

    fun getCategoryEvents(
        name: String,
        page: Int?,
        size: Int?
    ): PageDto<EventEntity> {
        try {
            val category = categoryRepo.findByName(name) ?: throw CategoryNotFoundException()
            return fromPage(
                eventsRepo.findByCategoriesIn(
                    listOf(category),
                    PageRequest.of(
                        (page ?: 1) - 1,
                        size ?: DEFAULT_SIZE
                    )
                )
            )
        } catch (e: IllegalArgumentException) {
            throw InvalidPageException("Invalid page or size given")
        }
    }

    fun updateCategory(id: String, request: CategoryRequest): Category {
        val category = categoryRepo.findByIdOrNull(id) ?: throw CategoryNotFoundException()
        return categoryRepo.save(
            category.copy(name = request.name)
        )
    }

    fun deleteCategory(name: String) {
        val category = categoryRepo.findByName(name) ?: throw CategoryNotFoundException()
        eventsRepo.saveAll(
            category.events.map {
                it.copy(
                    categories = it.categories.filter { cat ->
                        cat != category
                    }
                )
            }
        )
        categoryRepo.delete(category)
    }

    fun getCategoryByName(name: String): Category? {
        return categoryRepo.findByName(name)
    }
}
