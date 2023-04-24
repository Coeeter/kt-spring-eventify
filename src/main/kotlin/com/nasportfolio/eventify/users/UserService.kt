package com.nasportfolio.eventify.users

import com.nasportfolio.eventify.dtos.PageDto
import com.nasportfolio.eventify.dtos.PageDto.Companion.DEFAULT_SIZE
import com.nasportfolio.eventify.events.exceptions.InvalidPageException
import com.nasportfolio.eventify.events.models.entities.EventEntity
import com.nasportfolio.eventify.images.ImageService
import com.nasportfolio.eventify.security.SecurityProperties
import com.nasportfolio.eventify.users.exceptions.InvalidCredentialsException
import com.nasportfolio.eventify.users.exceptions.UserNotFoundException
import com.nasportfolio.eventify.users.models.UserEntity
import com.nasportfolio.eventify.users.models.requests.DeleteUserRequest
import com.nasportfolio.eventify.users.models.requests.UpdateUserRequest
import com.nasportfolio.eventify.users.models.responses.UserDeletedResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.util.UrlUtils
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepo: UserRepo,
    private val securityProperties: SecurityProperties,
    private val imageService: ImageService
) : UserDetailsService {
    fun getUsers(name: String?, page: Int?, size: Int?): PageDto<UserEntity> {
        try {
            return PageDto.fromPage(
                page = userRepo.searchByNameContainingIgnoreCase(
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

    fun getUserById(id: String): UserEntity {
        return userRepo.findByIdOrNull(
            id = id
        ) ?: throw UserNotFoundException(
            message = "User with id $id not found"
        )
    }

    fun getUserByEmail(email: String): UserEntity {
        return try {
            userRepo.findUserByEmail(email)
        } catch (e: Exception) {
            throw UserNotFoundException("User with email $email not found")
        }
    }

    fun saveUser(userEntity: UserEntity): UserEntity {
        return userRepo.save(userEntity)
    }

    fun updateUser(
        updateUserRequest: UpdateUserRequest,
        securityUser: User
    ): UserEntity {
        val user = getUserByEmail(securityUser.username)
        updateUserRequest.name?.let {
            if (it.isNotBlank()) return@let
            throw InvalidCredentialsException("Name is required")
        }
        updateUserRequest.image?.let {
            if (it.isBlank() || !UrlUtils.isAbsoluteUrl(it)) {
                throw InvalidCredentialsException("Invalid image provided")
            }
            user.imageUrl?.let { url -> imageService.deleteImage(url) }
        }
        return saveUser(
            user.copy(
                name = updateUserRequest.name ?: user.name,
                imageUrl = updateUserRequest.image ?: user.imageUrl
            )
        )
    }

    override fun loadUserByUsername(username: String?): UserDetails? {
        return try {
            val user = username?.let {
                userRepo.findUserByEmail(email = it)
            } ?: throw Exception()
            User(
                user.email,
                user.password,
                mutableListOf()
            )
        } catch (e: Exception) {
            null
        }
    }

    fun deleteUser(
        deleteUserRequest: DeleteUserRequest,
        securityUser: User
    ): UserDeletedResponse {
        val user = getUserByEmail(securityUser.username)
        val isValidPassword = BCryptPasswordEncoder(
            securityProperties.strength
        ).matches(
            deleteUserRequest.password,
            user.password
        )
        if (!isValidPassword) {
            throw InvalidCredentialsException("Invalid password given")
        }
        user.imageUrl?.let { imageService.deleteImage(it) }
        userRepo.deleteById(user.id)
        return UserDeletedResponse(user.id)
    }

    fun deleteImage(securityUser: User): UserEntity {
        val user = getUserByEmail(securityUser.username)
        user.imageUrl?.let { imageService.deleteImage(it) }
        return saveUser(user.copy(imageUrl = null))
    }

    fun getAttendeesOfEvent(
        eventEntity: EventEntity,
        pageable: Pageable
    ): Page<UserEntity> {
        return userRepo.findByAttendingEventsContaining(
            eventEntity,
            pageable
        )
    }
}