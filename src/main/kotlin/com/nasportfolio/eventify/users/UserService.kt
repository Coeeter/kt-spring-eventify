package com.nasportfolio.eventify.users

import com.nasportfolio.eventify.dtos.PageDto
import com.nasportfolio.eventify.dtos.PageDto.Companion.DEFAULT_SIZE
import com.nasportfolio.eventify.images.ImageService
import com.nasportfolio.eventify.security.SecurityProperties
import com.nasportfolio.eventify.users.exceptions.InvalidCredentialsException
import com.nasportfolio.eventify.users.exceptions.UserNotFoundException
import com.nasportfolio.eventify.users.models.UserEntity
import com.nasportfolio.eventify.users.models.dtos.UserDto
import com.nasportfolio.eventify.users.models.dtos.userDto
import com.nasportfolio.eventify.users.models.requests.DeleteUserRequest
import com.nasportfolio.eventify.users.models.requests.UpdateUserRequest
import com.nasportfolio.eventify.users.models.responses.UserDeletedResponse
import com.nasportfolio.eventify.utils.EventifyPageRequest
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
    private val imageService: ImageService,
) : UserDetailsService {
    fun getUsers(name: String?, page: Int?, size: Int?): PageDto<UserDto> {
        return PageDto.fromPage(
            page = userRepo.searchByNameContainingIgnoreCase(
                name = name ?: "",
                pageable = EventifyPageRequest(
                    (page ?: 1) - 1,
                    size ?: DEFAULT_SIZE,
                )
            ),
            transform = { it.userDto }
        )
    }

    fun getUserById(id: String): UserDto {
        return userRepo.findByIdOrNull(
            id = id
        )?.userDto ?: throw UserNotFoundException(
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
    ): UserDto {
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
        ).userDto
    }

    override fun loadUserByUsername(username: String?): UserDetails? {
        return try {
            val user = username?.let {
                getUserByEmail(it)
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

    fun deleteImage(securityUser: User): UserDto {
        val user = getUserByEmail(securityUser.username)
        user.imageUrl?.let { imageService.deleteImage(it) }
        return saveUser(user.copy(imageUrl = null)).userDto
    }
}