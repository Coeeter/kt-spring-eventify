package com.nasportfolio.eventify.users

import com.nasportfolio.eventify.events.models.entities.EventEntity
import com.nasportfolio.eventify.users.models.UserEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepo : JpaRepository<UserEntity, String> {
    fun findUserByEmail(email: String): UserEntity
    fun findByAttendingEventsContaining(eventEntity: EventEntity, pageable: Pageable): Page<UserEntity>
    fun searchByNameContainingIgnoreCase(name: String, pageable: Pageable): Page<UserEntity>
}