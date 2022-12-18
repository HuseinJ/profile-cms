package com.hjusic.api.profileapi.user.application

import com.hjusic.api.profileapi.common.error.ValidationError
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.common.result.Either
import com.hjusic.api.profileapi.user.model.User
import com.hjusic.api.profileapi.user.model.Users
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder

class ChangePassword(
    val authenticationManager: AuthenticationManager,
    val users: Users,
    val passwordEncoder: PasswordEncoder
) {
    fun changePassword(callingUser: User, oldPassword: String, newPassword: String): Either<ValidationError, User> {
        if(oldPassword == "" || newPassword == "") {
            return Either.wasFailure(ValidationError(ValidationErrorCode.EMPTY_VALUE))
        }

        try{
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(callingUser.name, oldPassword));
        }catch (badCredentialException: BadCredentialsException){
            return Either.wasFailure(ValidationError(ValidationErrorCode.WRONG_CREDENTIALS))
        }catch (exception: Exception){
            return Either.wasFailure(ValidationError(ValidationErrorCode.EXCEPTION))
        }

        return Either.wasSuccess(users.trigger(callingUser.changePassword(passwordEncoder.encode(newPassword))))

    }
}