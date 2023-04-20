package com.nasportfolio.eventify.auth

import com.nasportfolio.eventify.auth.models.entities.PasswordResetTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PasswordResetTokenRepo : JpaRepository<PasswordResetTokenEntity, String> {
    fun findByToken(token: String): PasswordResetTokenEntity?
    fun findByUserId(userId: String): List<PasswordResetTokenEntity>
}