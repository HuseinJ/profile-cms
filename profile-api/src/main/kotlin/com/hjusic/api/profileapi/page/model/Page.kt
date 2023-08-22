package com.hjusic.api.profileapi.page.model

import com.hjusic.api.profileapi.accessRole.model.AccessRight
import com.hjusic.api.profileapi.common.error.DomainError
import com.hjusic.api.profileapi.common.result.Either
import com.hjusic.api.profileapi.page.application.DeletePage
import com.hjusic.api.profileapi.pageComponent.model.PageComponent
import com.hjusic.api.profileapi.pageComponent.model.PageComponentAdded
import com.hjusic.api.profileapi.user.model.User
import java.util.*

abstract class Page(
    var id: UUID,
    var name: String
) {

    fun deletePage(callingUser: User): Either<DomainError, PageDeleted>{
        if (!callingUser.roles.stream().flatMap { role -> role.accessRights.stream() }
                .anyMatch { accessRight -> accessRight == AccessRight.CREATE_PAGE }) {
            return Either.wasFailure(DomainError(PageDomainErrorCode.USER_IS_NOT_ALLOWED_TO_DELETE_PAGE.name))
        }

        return Either.wasSuccess(PageDeleted.now(this))
    }

    fun addComponent(pageComponent: PageComponent, callingUser: User): Either<DomainError, PageComponentAdded>{
        if (!callingUser.roles.stream().flatMap { role -> role.accessRights.stream() }
                .anyMatch { accessRight -> accessRight == AccessRight.MODIFY_PAGE}) {
            return Either.wasFailure(DomainError(PageDomainErrorCode.USER_IS_NOT_ALLOWED_TO_MODIFY_PAGE.name))
        }

        return Either.wasSuccess(PageComponentAdded(pageComponent, this))
    }

    fun assignHomePage(callingUser: User): Either<DomainError, HomePageAssigned>{
        if (!callingUser.roles.stream().flatMap { role -> role.accessRights.stream() }
                .anyMatch { accessRight -> accessRight == AccessRight.ASSIGN_HOMEPAGE}) {
            return Either.wasFailure(DomainError(PageDomainErrorCode.USER_IS_NOT_ALLOWED_TO_ASSIGN_HOMEPAGE.name))
        }

        return  Either.wasSuccess(HomePageAssigned.now(this))
    }

}