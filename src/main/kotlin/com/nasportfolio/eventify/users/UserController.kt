package com.nasportfolio.eventify.users

import com.nasportfolio.eventify.users.models.UserEntity
import com.nasportfolio.eventify.users.models.requests.DeleteUserRequest
import com.nasportfolio.eventify.users.models.requests.UpdateUserRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
) {
    @GetMapping
    fun getAllUsers(
        @RequestParam("name") name: String?
    ): List<UserEntity> {
        return name?.let {
            userService.searchUsersByName(it)
        } ?: userService.getAllUsers()
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable("id") id: String): UserEntity {
        return userService.getUserById(id)
    }

    @PutMapping
    fun updateUser(
        @Valid @RequestBody request: UpdateUserRequest,
        @AuthenticationPrincipal user: User
    ): UserEntity {
        return userService.updateUser(request, user)
    }

    @DeleteMapping
    fun deleteUser(
        @Valid @RequestBody request: DeleteUserRequest,
        @AuthenticationPrincipal user: User
    ) {
        return userService.deleteUser(request, user)
    }

    @PostMapping("/images")
    fun uploadImage(
        @RequestParam("image") file: MultipartFile,
        @AuthenticationPrincipal user: User
    ): UserEntity {
        return userService.uploadImage(file, user)
    }

    @DeleteMapping("/images")
    fun uploadImage(
        @AuthenticationPrincipal user: User
    ): UserEntity {
        return userService.deleteImage( user)
    }
}