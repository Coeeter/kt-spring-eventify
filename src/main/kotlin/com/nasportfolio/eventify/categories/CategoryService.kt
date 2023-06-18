package com.nasportfolio.eventify.categories

import com.nasportfolio.eventify.categories.exceptions.CategoryAlreadyExistsException
import com.nasportfolio.eventify.categories.exceptions.CategoryNotFoundException
import com.nasportfolio.eventify.categories.models.Category
import com.nasportfolio.eventify.categories.models.dtos.CategoryDto
import com.nasportfolio.eventify.categories.models.dtos.categoryDto
import com.nasportfolio.eventify.categories.models.requests.CategoryRequest
import com.nasportfolio.eventify.dtos.PageDto
import com.nasportfolio.eventify.dtos.PageDto.Companion.DEFAULT_SIZE
import com.nasportfolio.eventify.dtos.PageDto.Companion.fromPage
import com.nasportfolio.eventify.events.EventsRepo
import com.nasportfolio.eventify.events.models.dtos.EventDto
import com.nasportfolio.eventify.events.models.dtos.eventDto
import com.nasportfolio.eventify.utils.EventifyPageRequest
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

    fun getCategories(name: String?, page: Int?, size: Int?): PageDto<CategoryDto> {
        return PageDto.fromPage(
            page = categoryRepo.findByNameContainingIgnoreCase(
                name = name ?: "",
                pageable = EventifyPageRequest(
                    page = (page ?: 1) - 1,
                    size = size ?: DEFAULT_SIZE
                )
            ),
            transform = { it.categoryDto }
        )
    }

    fun createCategory(request: CategoryRequest): CategoryDto {
        categoryRepo.findByName(request.name.lowercase())?.let {
            throw CategoryAlreadyExistsException()
        }
        return categoryRepo.save(
            Category(name = request.name.lowercase())
        ).categoryDto
    }

    fun getCategoryEvents(
        name: String,
        page: Int?,
        size: Int?
    ): PageDto<EventDto> {
        val category = categoryRepo.findByName(name) ?: throw CategoryNotFoundException()
        return fromPage(
            eventsRepo.findByCategoriesIn(
                listOf(category),
                EventifyPageRequest(
                    (page ?: 1) - 1,
                    size ?: DEFAULT_SIZE
                )
            ),
            transform = { it.eventDto }
        )
    }

    fun updateCategory(id: String, request: CategoryRequest): CategoryDto {
        val category = categoryRepo.findByIdOrNull(id) ?: throw CategoryNotFoundException()
        return categoryRepo.save(
            category.copy(name = request.name)
        ).categoryDto
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

    fun getCategoryByName(name: String): Category {
        return categoryRepo.findByName(name) ?: throw CategoryNotFoundException()
    }
}
