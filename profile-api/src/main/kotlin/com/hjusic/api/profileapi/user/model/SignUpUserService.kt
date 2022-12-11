package com.hjusic.api.profileapi.user.model

import com.hjusic.api.profileapi.accessRole.model.AccessRight
import com.hjusic.api.profileapi.accessRole.model.AccessRoleService
import com.hjusic.api.profileapi.common.error.DomainError
import com.hjusic.api.profileapi.common.result.Either
import java.util.*

class SignUpUserService(var users: Users)
{
    fun createUserWithNameAndMail(
        name: String,
        password: String,
        email: String,
        callingUser: User
    ): Either<DomainError, UserCreated> {

        if (!callingUser.roles.stream().flatMap { role -> role.accessRights.stream() }
                .anyMatch { accessRight -> accessRight == AccessRight.CREATE_USER }) {
            return Either.wasFailure(DomainError(UserDomainErrorCode.USER_IS_NOT_PERMITTED_TO_CREATE_USERS.name))
        }

        if(users.existsByName(name)){
            return Either.wasFailure(DomainError(UserDomainErrorCode.USER_NAME_IS_ALREADY_TAKEN.name))
        }

        if(users.existsByemail(name)){
            return Either.wasFailure(DomainError(UserDomainErrorCode.USER_MAIL_IS_ALREADY_TAKEN.name))
        }

        return Either.wasSuccess(
            UserCreated(
                User(
                    UUID.randomUUID(),
                    name,
                    email,
                    setOf(AccessRoleService.guestRole())
                ), password
            )
        )


    }
}