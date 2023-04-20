package com.nasportfolio.eventify.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

@ConfigurationProperties(prefix = "jwt")
@Validated
class SecurityProperties {
    @field:NotBlank
    @field:Size(min = 64)
    var secret: String = ""
    @field:NotBlank
    var issuer: String = ""
    @field:Positive
    var expirationTime: Int = 31
    @field:Positive
    var strength: Int = 10
    var tokenPrefix: String = "Bearer "
    var headerString: String = "Authorization"
}