package com.hjusic.api.profileapi.user.application

import com.hjusic.api.profileapi.common.error.ContextError
import com.hjusic.api.profileapi.common.error.ValidationError
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.common.result.Either
import com.hjusic.api.profileapi.common.security.UserAuthServices
import com.hjusic.api.profileapi.user.model.SignUpUserService
import com.hjusic.api.profileapi.user.model.User
import com.hjusic.api.profileapi.user.model.Users
import org.springframework.security.crypto.password.PasswordEncoder

class SignUpUser(
    val signUpUserService: SignUpUserService,
    val users: Users,
    val userAuthServices: UserAuthServices,
    val passwordEncoder: PasswordEncoder,
) {
    fun createUserWithNameAndMail(name: String, password: String, email: String): Either<ContextError, User> {
        if (name.isEmpty()) {
            return Either.wasFailure(ValidationError(ValidationErrorCode.EMPTY_VALUE))
        }
        if (email.isEmpty()) {
            return Either.wasFailure(ValidationError(ValidationErrorCode.EMPTY_VALUE))
        }
        if (password.isEmpty()) {
            return Either.wasFailure(ValidationError(ValidationErrorCode.EMPTY_VALUE))
        }

        var callingUser = userAuthServices.callingUser()

        val potentialUserCreated =
            signUpUserService.createUserWithNameAndMail(name, passwordEncoder.encode(password), email, callingUser)

        if (potentialUserCreated.fail != null) {
            return Either.wasFailure(potentialUserCreated.fail)
        }

        return Either.wasSuccess(this.users.trigger(potentialUserCreated.success!!))
    }
}