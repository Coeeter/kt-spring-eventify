package com.nasportfolio.eventify.categories

import com.nasportfolio.eventify.categories.models.Category
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepo : JpaRepository<Category, String> {
    fun findByName(name: String): Category?
    fun findByNameIn(names: List<String>): List<Category>
    fun findByNameContainingIgnoreCase(name: String, pageable: Pageable): Page<Category>
}
