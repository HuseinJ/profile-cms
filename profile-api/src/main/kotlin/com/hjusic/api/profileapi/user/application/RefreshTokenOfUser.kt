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
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import java.time.Instant
import java.util.*


class RefreshTokenOfUser(
    val users: Users,
    val jwtUtils: JwtUtils
) {

    companion object {
        @Value("\${auth.jwtRefreshExpirationMs}")
        private val refreshTokenDurationMs: Long = 1L
    }


    fun refreshTokenOfUser(refreshToken: String, authenticatedUser: User): Either<ContextError, UserTokenTuple> {
        if (refreshToken == "") {
            return Either.wasFailure(ValidationError(ValidationErrorCode.EMPTY_VALUE))
        }

        var refreshTokenofUser = users.findRefreshTokenOfUser(authenticatedUser)
            ?: return Either.wasFailure(ValidationError(ValidationErrorCode.WRONG_CREDENTIALS))

        if(refreshToken != refreshTokenofUser.token) {
            return Either.wasFailure(ValidationError(ValidationErrorCode.WRONG_CREDENTIALS))
        }

        if(isTokenExpired(refreshTokenofUser)){
            //TODO: DELETE OLD REFRESH TOKEN OF USER
            return Either.wasFailure(ValidationError(ValidationErrorCode.EXPIRED_CREDENTIALS))
        }

        val jwt = jwtUtils.generateJwtToken(SecurityContextHolder.getContext().authentication)
        return Either.wasSuccess(UserTokenTuple(authenticatedUser, jwt, refreshTokenofUser.token))

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