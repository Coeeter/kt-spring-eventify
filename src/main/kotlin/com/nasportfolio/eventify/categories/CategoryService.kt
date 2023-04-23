package com.nasportfolio.eventify.categories

import com.nasportfolio.eventify.categories.models.Category
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryRepo: CategoryRepo
) {
    fun getCategoriesByNames(categories: List<String>): List<Category> {
        return categoryRepo.findByNameIn(categories)
    }

}
