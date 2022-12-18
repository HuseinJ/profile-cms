package com.hjusic.api.profileapi.page.model

import com.hjusic.api.profileapi.accessRole.model.AccessRight
import com.hjusic.api.profileapi.common.error.DomainError
import com.hjusic.api.profileapi.common.result.Either
import com.hjusic.api.profileapi.user.model.User

class CreatePageService {

    fun createPage(callingUser: User, name: String): Either<DomainError, PageCreated> {
        if (!callingUser.roles.stream().flatMap { role -> role.accessRights.stream() }
                .anyMatch { accessRight -> accessRight == AccessRight.CREATE_PAGE }) {
            return Either.wasFailure(DomainError(PageDomainErrorCode.USER_IS_NOT_ALLOWED_TO_CREATE_PAGE.name))
        }

        return Either.wasSuccess(PageCreated.now(name));
    }
}