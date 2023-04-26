package com.hjusic.api.profileapi.user.application

import com.hjusic.api.profileapi.common.error.ContextError
import com.hjusic.api.profileapi.common.error.ValidationError
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.common.result.Either
import com.hjusic.api.profileapi.common.security.service.UserDetailsImpl
import com.hjusic.api.profileapi.common.security.util.JwtUtils
import com.hjusic.api.profileapi.user.model.Users
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

class SignInUser(
    val authenticationManager: AuthenticationManager,
    val refreshTokenOfUser: RefreshTokenOfUser,
    val users: Users,
    val jwtUtils: JwtUtils
) {

    fun signInUser(username: String, password: String): Either<ContextError, UserTokenTuple>{
        if(username == "" || password == ""){
            return Either.wasFailure(ValidationError(ValidationErrorCode.EMPTY_VALUE))
        }
        try {
            var authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
            SecurityContextHolder.getContext().authentication = authentication
            val jwt = jwtUtils.generateJwtToken(authentication)
            val userDetails = authentication.principal as UserDetailsImpl
            var user = users.findByName(userDetails.username)
            user = refreshTokenOfUser.createRefreshTokenForUser(user)
            return Either.wasSuccess(UserTokenTuple(user, jwt, user.refreshToken?.token))
        }catch (e: java.lang.Exception){
            return Either.wasFailure(ValidationError(ValidationErrorCode.WRONG_CREDENTIALS))
        }
    }
}