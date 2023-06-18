package com.nasportfolio.eventify.auth

import com.nasportfolio.eventify.auth.models.dtos.PasswordResetTokenDto
import com.nasportfolio.eventify.auth.models.entities.PasswordResetTokenEntity
import com.nasportfolio.eventify.auth.models.requests.*
import com.nasportfolio.eventify.auth.models.responses.TokenResponse
import com.nasportfolio.eventify.users.models.UserEntity
import com.nasportfolio.eventify.users.models.dtos.UserDto
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/auth")
@Validated
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): TokenResponse {
        return authService.login(request)
    }

    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): TokenResponse {
        return authService.register(request)
    }

    @PostMapping("/forget-password")
    fun forgetPassword(@Valid @RequestBody request: ForgetPasswordRequest) {
        return authService.forgetPassword(request)
    }

    @GetMapping("/token/{token}")
    fun getTokenDetails(@PathVariable("token") token: String): PasswordResetTokenDto {
        return authService.getTokenDetails(token)
    }

    @PutMapping("/reset-password")
    fun resetPassword(@Valid @RequestBody request: ResetPasswordRequest) {
        return authService.resetPassword(request)
    }

    @PutMapping("/password")
    fun changePassword(
        @Valid @RequestBody request: ChangePasswordRequest,
        @AuthenticationPrincipal user: User
    ) {
        return authService.changePassword(request, user)
    }
}