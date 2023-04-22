package com.nasportfolio.eventify.categories

import com.nasportfolio.eventify.categories.models.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepo : JpaRepository<Category, String> {
    @Query("SELECT c FROM Category c WHERE c.name IN :names")
    fun findByNameWithList(names: List<String>): List<Category>
}
