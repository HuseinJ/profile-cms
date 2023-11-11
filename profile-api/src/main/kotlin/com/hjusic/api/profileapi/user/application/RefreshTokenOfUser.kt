package com.hjusic.api.profileapi.user.application

import com.hjusic.api.profileapi.common.error.ContextError
import com.hjusic.api.profileapi.common.error.ValidationError
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.common.result.Either
import com.hjusic.api.profileapi.common.security.util.JwtUtils
import com.hjusic.api.profileapi.user.model.RefreshToken
import com.hjusic.api.profileapi.user.model.User
import com.hjusic.api.profileapi.user.model.Users
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import java.time.Instant
import java.util.*


class RefreshTokenOfUser(
    val users: Users,
    val jwtUtils: JwtUtils,
    val userDetailsService: UserDetailsService
) {

    @Value("\${auth.jwtRefreshExpirationMs}")
    private val refreshTokenDurationMs: Long = 1L

    fun refreshTokenOfUser(refreshToken: String): Either<ContextError, UserTokenTuple> {
        if (refreshToken == "") {
            return Either.wasFailure(ValidationError(ValidationErrorCode.EMPTY_VALUE))
        }

        var userForToken = users.findUserByRefreshToken(refreshToken)
            ?: return Either.wasFailure(ValidationError(ValidationErrorCode.WRONG_CREDENTIALS))

        if(isTokenExpired(userForToken.refreshToken!!)){
            //TODO: DELETE OLD REFRESH TOKEN OF USER
            return Either.wasFailure(ValidationError(ValidationErrorCode.EXPIRED_CREDENTIALS))
        }

        val jwt = jwtUtils.generateJwtToken(getAuthenticationByUser(userForToken))
        return Either.wasSuccess(UserTokenTuple(userForToken, jwt, userForToken.refreshToken!!.token))
    }

    private fun getAuthenticationByUser(user: User): Authentication {
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(user.name)!!
        return UsernamePasswordAuthenticationToken(
            userDetails, null,
            userDetails.authorities
        )
    }

    fun createRefreshTokenForUser(authenticatedUser: User): User {
        var token = UUID.randomUUID().toString();
        var expirationDate = Instant.now().plusMillis(refreshTokenDurationMs)
        return users.trigger(authenticatedUser.createRefreshToken(token, expirationDate));
    }

    private fun isTokenExpired(token: RefreshToken): Boolean {
        return token.expirationDate < Instant.now()
    }
}