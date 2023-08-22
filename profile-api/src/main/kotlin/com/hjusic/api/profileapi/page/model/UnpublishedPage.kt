package com.hjusic.api.profileapi.page.model

import com.hjusic.api.profileapi.accessRole.model.AccessRight
import com.hjusic.api.profileapi.common.error.DomainError
import com.hjusic.api.profileapi.common.result.Either
import com.hjusic.api.profileapi.user.model.User
import java.util.*

class UnpublishedPage(
    id: UUID,
    name: String
    ): Page(id, name) {

    fun assignHomePage(callingUser: User): Either<DomainError, HomePageAssigned> {
        if (!callingUser.roles.stream().flatMap { role -> role.accessRights.stream() }
                .anyMatch { accessRight -> accessRight == AccessRight.ASSIGN_HOMEPAGE}) {
            return Either.wasFailure(DomainError(PageDomainErrorCode.USER_IS_NOT_ALLOWED_TO_ASSIGN_HOMEPAGE.name))
        }

        return  Either.wasSuccess(HomePageAssigned.now(this))
    }
}