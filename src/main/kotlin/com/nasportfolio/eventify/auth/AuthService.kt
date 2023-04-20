package com.nasportfolio.eventify.auth

import com.nasportfolio.eventify.auth.exceptions.EmailAlreadyInUseException
import com.nasportfolio.eventify.auth.exceptions.InvalidTokenException
import com.nasportfolio.eventify.auth.models.entities.PasswordResetTokenEntity
import com.nasportfolio.eventify.auth.models.requests.*
import com.nasportfolio.eventify.auth.models.responses.TokenResponse
import com.nasportfolio.eventify.mail.EmailService
import com.nasportfolio.eventify.security.jwt.JwtService
import com.nasportfolio.eventify.users.UserService
import com.nasportfolio.eventify.users.exceptions.UserNotFoundException
import com.nasportfolio.eventify.users.models.UserEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(
    private val userService: UserService,
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder,
    private val passwordResetTokenRepo: PasswordResetTokenRepo,
    private val emailService: EmailService
) {
    fun login(loginRequest: LoginRequest): TokenResponse {
        userService.loadUserByUsername(
            loginRequest.email
        ) ?: throw UserNotFoundException("User not found")
        val auth = UsernamePasswordAuthenticationToken(
            loginRequest.email,
            loginRequest.password
        )
        val user = authenticationManager
            .authenticate(auth)
            .principal as User
        return TokenResponse(
            token = jwtService.generateJwt(user)
        )
    }

    fun register(registerRequest: RegisterRequest): TokenResponse {
        userService.loadUserByUsername(registerRequest.email)?.let {
            throw EmailAlreadyInUseException("Email already taken")
        }
        val user = userService.saveUser(
            userEntity = UserEntity(
                name = registerRequest.name,
                email = registerRequest.email,
                password = passwordEncoder.encode(registerRequest.password),
                role = registerRequest.role
            )
        ).run {
            val authorities = mutableListOf(SimpleGrantedAuthority(role.name))
            User(email, password, authorities)
        }
        return TokenResponse(
            jwtService.generateJwt(user)
        )
    }

    fun forgetPassword(forgetPasswordRequest: ForgetPasswordRequest) {
        val user = userService.getUserByEmail(forgetPasswordRequest.email)
        passwordResetTokenRepo.findByUserId(user.id)
            .filter {
                !it.disabled && it.resetAt == null && it.expiresAt.time > System.currentTimeMillis()
            }
            .forEach {
                passwordResetTokenRepo.save(
                    it.copy(disabled = true)
                )
            }
        val token = PasswordResetTokenEntity(
            expiresAt = Date(
                System.currentTimeMillis() + 15L * 60L * 1000L
            ),
            user = user
        ).also {
            passwordResetTokenRepo.save(it)
        }
        val url = "${System.getenv("CLIENT_URL")}/reset-password/${token.token}"
        emailService.sendPasswordResetEmail(
            to = user.email,
            url = url
        )
    }

    fun getTokenDetails(token: String): PasswordResetTokenEntity {
        return passwordResetTokenRepo.findByToken(
            token = token
        ) ?: throw InvalidTokenException(
            message = "Invalid token provided"
        )
    }

    fun resetPassword(resetPasswordRequest: ResetPasswordRequest) {
        val token = passwordResetTokenRepo
            .findByToken(resetPasswordRequest.token)
            ?: throw InvalidTokenException("Invalid token provided")
        if (token.disabled || token.resetAt != null || token.expiresAt.time < System.currentTimeMillis()) {
            throw InvalidTokenException("Invalid token provided")
        }
        userService.saveUser(
            token.user.copy(
                password = passwordEncoder.encode(resetPasswordRequest.password)
            )
        )
        passwordResetTokenRepo.save(
            token.copy(
                resetAt = Date()
            )
        )
    }

    fun changePassword(
        request: ChangePasswordRequest,
        securityUser: User
    ): UserEntity {
        val user = userService.getUserByEmail(securityUser.username)
        return userService.saveUser(
            user.copy(password = passwordEncoder.encode(request.password))
        )
    }
}