package com.hjusic.api.profileapi.common.security.util

import com.hjusic.api.profileapi.common.security.service.UserDetailsImpl
import io.jsonwebtoken.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtils {

    @Value("\${auth.jwtSecret}")
    private val jwtSecret: String? = null;

    @Value("\${auth.jwtExpirationMs}")
    private val jwtExpirationMs: Int = 0;

    fun generateJwtToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as UserDetailsImpl
        return Jwts.builder()
            .setSubject(userPrincipal.username)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }

    fun getUserNameFromJwtToken(token: String): String {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body.subject
    }

    fun validateJwtToken(authToken: String): Boolean {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken)
            return true
        } catch (e: SignatureException) {
            //TODO: implement logging errors
        } catch (e: MalformedJwtException) {
        } catch (e: ExpiredJwtException) {
        } catch (e: UnsupportedJwtException) {
        } catch (e: IllegalArgumentException) {
        }
        return false
    }

}