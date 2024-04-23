package com.framework.config.security.jwt

import com.framework.module.auth.entities.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.constraints.Min
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*
import javax.crypto.SecretKey


@Configuration
@ConfigurationProperties(prefix = "jwt")
class JwtProps {
    var key: String = ""

    @field:Min(600000)
    var accessTokenExp: Long = -1

    @field:Min(600000)
    var refreshTokenExp: Long = -1
}

@Component
@EnableConfigurationProperties(JwtProps::class)
class JwtUtils(private val jwtProps: JwtProps) {

    val secretKey: SecretKey? = Keys.hmacShaKeyFor(jwtProps.key.toByteArray())
    fun generateJwtToken(user: User, additionalClaims: Map<String, Any> = emptyMap()): String =
        Jwts.builder()
            .claims()
            .subject(user.username)
            .add("user_id", user.id)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + jwtProps.accessTokenExp))
            .add(additionalClaims)
            .and()
            .signWith(secretKey)
            .compact()


    fun getClaims(jwtToken: String): Claims = Jwts.parser()
        .verifyWith(secretKey).build()
        .parseSignedClaims(jwtToken)
        .payload

    fun <T> getClaim(jwtToken: String, fn: (String) -> T) = fn.invoke(jwtToken)

    fun isValid(jwtToken: String, user: UserDetails) =
        getClaim(jwtToken) { s -> getClaims(s).subject } == user.username &&
                getClaim(jwtToken) { getClaims(jwtToken).expiration }.after(Date.from(Instant.now()))

    fun getJwtToken(httpServletRequest: HttpServletRequest): String? {
        if (httpServletRequest.getHeader("Authorization") != null && httpServletRequest.getHeader("Authorization")
                .startsWith("Bearer ")
        )
            return httpServletRequest.getHeader("Authorization").substringAfter("Bearer ")
        return null
    }
}
