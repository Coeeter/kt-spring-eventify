package com.nasportfolio.eventify.categories

import com.nasportfolio.eventify.categories.models.Category
import com.nasportfolio.eventify.categories.models.requests.CategoryRequest
import com.nasportfolio.eventify.dtos.PageDto
import com.nasportfolio.eventify.events.models.entities.EventEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/categories")
class CategoryController(
    private val categoryService: CategoryService
) {
    @GetMapping
    fun getCategories(
        @RequestParam name: String?,
        @RequestParam page: Int?,
        @RequestParam size: Int?,
    ): PageDto<Category> {
        return categoryService.getCategories(name, page, size)
    }

    @GetMapping("/{name}/events")
    fun getEventsOfCategory(
        @PathVariable name: String,
        @RequestParam page: Int?,
        @RequestParam size: Int?
    ): PageDto<EventEntity> {
        return categoryService.getCategoryEvents(name, page, size)
    }

    @PostMapping
    fun createCategory(
        @Valid @RequestBody request: CategoryRequest
    ): Category {
        return categoryService.createCategory(request)
    }

    @PutMapping("/{id}")
    fun updateCategory(
        @PathVariable id: String,
        @Valid @RequestBody request: CategoryRequest
    ): Category {
        return categoryService.updateCategory(id, request)
    }

    @DeleteMapping("{name}")
    fun deleteCategory(
        @PathVariable name: String
    ) {
        return categoryService.deleteCategory(name)
    }
}