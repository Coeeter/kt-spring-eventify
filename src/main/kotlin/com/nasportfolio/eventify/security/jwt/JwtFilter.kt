package com.nasportfolio.eventify.security.jwt

import com.nasportfolio.eventify.security.SecurityProperties
import com.nasportfolio.eventify.users.UserService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtFilter(
    private val jwtService: JwtService,
    private val userService: UserService,
    private val securityProperties: SecurityProperties
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            SecurityContextHolder.getContext().authentication?.let {
                throw Exception()
            }
            val header = request.getHeader(securityProperties.headerString) ?: throw Exception()
            if (!header.startsWith(securityProperties.tokenPrefix)) throw Exception()
            val jwt = header.removePrefix(securityProperties.tokenPrefix)
            val decodedUserEmail = jwtService.validateJwt(jwt) ?: throw Exception()
            val decodedUser = userService.loadUserByUsername(decodedUserEmail) ?: throw Exception()
            SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
                decodedUser,
                null,
                decodedUser.authorities
            ).apply {
                details = WebAuthenticationDetailsSource().buildDetails(request)
            }
        } catch (e: Exception) {
            println("FILTER EXCEPTION: ${e.message}")
        }
        filterChain.doFilter(request, response)
    }
}