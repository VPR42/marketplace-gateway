package com.vpr42.marketplacegateway.security

import com.vpr42.marketplacegateway.properties.ApplicationProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService(
    private val applicationProperties: ApplicationProperties,
) {
    private val signKey = Keys.hmacShaKeyFor(
        Decoders.BASE64.decode(applicationProperties.auth.secret)
    )

    // Валидаторы
    private fun isTokenExpired(token: String): Boolean = extractClaims(token).expiration.before(Date())

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractClaims(token).subject
        return username == userDetails.username && !isTokenExpired(token)
    }

    // Экстракторы
    fun getLogin(token: String): String = extractClaims(token).subject

    fun getId(token: String): String = extractClaims(token)["id"].toString()

    private fun extractClaims(token: String): Claims = Jwts
        .parser()
        .verifyWith(signKey)
        .build()
        .parseSignedClaims(token)
        .payload
}
