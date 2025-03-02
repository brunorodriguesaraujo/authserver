package br.brunorodrigues.authserver.security

import br.brunorodrigues.authserver.users.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.jackson.io.JacksonDeserializer
import io.jsonwebtoken.jackson.io.JacksonSerializer
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

@Component
class JWT(
    @Value("\${security.token.secret}") private val secret: String,
    @Value("\${security.token.issuer}") private val issuer: String,
    @Value("\${security.token.expire-hours}") private val expireHours: Long,
    @Value("\${security.token.admin-expire-hours}") private val adminExpireHours: Long
) {
    fun createToken(user: User): String =
        UserToken(user).let {
            Jwts.builder().json(JacksonSerializer())
                .signWith(Keys.hmacShaKeyFor(secret.toByteArray()))
                .subject(user.id.toString())
                .issuedAt(utcNow().toDate())
                .expiration(
                    utcNow().plusHours(
                        if (it.isAdmin) adminExpireHours else expireHours
                    ).toDate()
                )
                .issuer(issuer)
                .claim(USER_FIELD, it)
                .compact()
        }

    fun extract(req: HttpServletRequest): Authentication? {
        try {
            val header = req.getHeader(AUTHORIZATION)
            if (header == null || !header.startsWith("Bearer ")) return null
            val token = header.removePrefix("Bearer ")
            if (token.isEmpty()) return null

            val claims = Jwts.parser().json(JacksonDeserializer(mapOf(USER_FIELD to UserToken::class.java)))
                .verifyWith(Keys.hmacShaKeyFor(secret.toByteArray()))
                .build()
                .parseSignedClaims(token).payload

            if (claims.issuer != issuer) return null
            return claims.get("user", UserToken::class.java).toAuthentication()

        } catch (e: Throwable) {
            log.warn("Token rejected", e)
            return null
        }
    }

    companion object {
        val log = LoggerFactory.getLogger(JWT::class.java)
        const val USER_FIELD = "user"

        private fun utcNow() = ZonedDateTime.now(ZoneOffset.UTC)
        private fun ZonedDateTime.toDate(): Date = Date.from(this.toInstant())
        private fun UserToken.toAuthentication(): Authentication {
            val authorities = this.roles.map { SimpleGrantedAuthority("ROLE_$it") }
            return UsernamePasswordAuthenticationToken(this, id, authorities)
        }
    }
}
