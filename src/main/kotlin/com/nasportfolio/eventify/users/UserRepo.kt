package com.nasportfolio.eventify.users

import com.nasportfolio.eventify.users.models.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepo : JpaRepository<UserEntity, String> {
    fun findUserByEmail(email: String): UserEntity

    @Query(
        "SELECT u from UserEntity u WHERE u.name LIKE CONCAT('%', :name, '%')"
    )
    fun searchUsersByName(name: String): List<UserEntity>
}