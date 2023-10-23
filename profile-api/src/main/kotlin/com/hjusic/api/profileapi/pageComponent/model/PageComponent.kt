package com.hjusic.api.profileapi.pageComponent.model

import com.hjusic.api.profileapi.accessRole.model.AccessRight
import com.hjusic.api.profileapi.common.error.DomainError
import com.hjusic.api.profileapi.common.result.Either
import com.hjusic.api.profileapi.page.model.Page
import com.hjusic.api.profileapi.page.model.PageDomainErrorCode
import com.hjusic.api.profileapi.page.model.UnpublishedPage
import com.hjusic.api.profileapi.user.model.User
import java.util.UUID

class PageComponent(
    val id: UUID,
    val componentName: PageComponentName,
    val componentData: Map<String, String>,
){
    private var order: Int? = null
        get() = field
        set(value) {
            field = value
        }
    var pageid: UUID? = null
        get() = field
        set(value) {
            field = value
        }

    fun removePageComponent(callingUser: User): Either<DomainError, PageComponentRemoved>{
        if (!callingUser.roles.stream().flatMap { role -> role.accessRights.stream() }
            .anyMatch { accessRight -> accessRight == AccessRight.MODIFY_PAGE}) {
            return Either.wasFailure(DomainError(PageDomainErrorCode.USER_IS_NOT_ALLOWED_TO_MODIFY_PAGE.name))
        }

        if(pageid == null) {
            return Either.wasFailure(DomainError(PageDomainErrorCode.NO_SUCH_PAGE_WITH_THIS_ID.name))
        }

        return Either.wasSuccess(PageComponentRemoved(this, UnpublishedPage(pageid!!, "")))
    }

}