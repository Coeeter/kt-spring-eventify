package com.nasportfolio.eventify.users

import com.nasportfolio.eventify.users.models.UserEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.jaxb.SpringDataJaxb.PageDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepo : JpaRepository<UserEntity, String> {
    fun findUserByEmail(email: String): UserEntity
    fun searchByNameContainingIgnoreCase(name: String, pageable: Pageable): Page<UserEntity>
}