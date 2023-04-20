package com.nasportfolio.eventify.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.nasportfolio.eventify.security.SecurityProperties
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService(
    private val securityProperties: SecurityProperties
) {
    private val expiresIn = securityProperties.expirationTime * 24L * 60L * 60L * 1000L

    fun generateJwt(
        user: User,
        secret: String = securityProperties.secret
    ): String {
        return JWT.create()
            .withIssuer(securityProperties.issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + expiresIn))
            .withSubject(user.username)
            .sign(Algorithm.HMAC256(secret))
    }

    fun validateJwt(
        jwt: String,
        secret: String = securityProperties.secret
    ): String? {
        return try {
            JWT.require(Algorithm.HMAC256(secret))
                .withIssuer(securityProperties.issuer)
                .build()
                .verify(jwt)
                .subject
        } catch (e: Exception) {
            null
        }
    }

}