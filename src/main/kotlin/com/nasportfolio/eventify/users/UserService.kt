package com.nasportfolio.eventify.users

import com.nasportfolio.eventify.images.ImageService
import com.nasportfolio.eventify.security.SecurityProperties
import com.nasportfolio.eventify.users.exceptions.InvalidCredentialsException
import com.nasportfolio.eventify.users.exceptions.UserNotFoundException
import com.nasportfolio.eventify.users.models.UserEntity
import com.nasportfolio.eventify.users.models.requests.DeleteUserRequest
import com.nasportfolio.eventify.users.models.requests.UpdateUserRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class UserService(
    private val userRepo: UserRepo,
    private val securityProperties: SecurityProperties,
    private val imageService: ImageService
) : UserDetailsService {
    fun getAllUsers(): List<UserEntity> {
        return userRepo.findAll()
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

    fun searchUsersByName(name: String): List<UserEntity> {
        return userRepo.searchUsersByName(name)
    }

    fun saveUser(userEntity: UserEntity): UserEntity {
        return userRepo.save(userEntity)
    }

    fun updateUser(
        updateUserRequest: UpdateUserRequest,
        securityUser: User
    ): UserEntity {
        val user = getUserByEmail(securityUser.username)
        return saveUser(
            user.copy(
                name = updateUserRequest.name,
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
                mutableListOf(
                    SimpleGrantedAuthority(user.role.name)
                )
            )
        } catch (e: Exception) {
            null
        }
    }

    fun deleteUser(
        deleteUserRequest: DeleteUserRequest,
        securityUser: User
    ) {
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
        userRepo.deleteById(user.id)
    }

    fun uploadImage(file: MultipartFile, securityUser: User): UserEntity {
        val url = imageService.uploadImage(file)
        val user = getUserByEmail(securityUser.username)
        user.imageUrl?.let { imageService.deleteImage(it) }
        return saveUser(user.copy(imageUrl = url))
    }

    fun deleteImage(securityUser: User): UserEntity {
        val user = getUserByEmail(securityUser.username)
        user.imageUrl?.let { imageService.deleteImage(it) }
        return saveUser(user.copy(imageUrl = null))
    }
}