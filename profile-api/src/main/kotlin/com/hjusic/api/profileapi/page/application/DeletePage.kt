package com.hjusic.api.profileapi.page.application

import com.hjusic.api.profileapi.common.error.ContextError
import com.hjusic.api.profileapi.common.error.DomainError
import com.hjusic.api.profileapi.common.error.ValidationError
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.common.result.Either
import com.hjusic.api.profileapi.page.model.Page
import com.hjusic.api.profileapi.page.model.PageDomainErrorCode
import com.hjusic.api.profileapi.page.model.Pages
import com.hjusic.api.profileapi.user.model.User
import java.util.UUID

class DeletePage(
    val pages: Pages
    ) {

    fun deletePage(possibleId: String, callingUser: User): Either<ContextError, Page> {

        if(possibleId == ""){
            return Either.wasFailure(ValidationError(ValidationErrorCode.EMPTY_VALUE))
        }

        var pageId = UUID.fromString(possibleId);
        var page = pages.findPageById(pageId);

        if(page.isEmpty){
            return Either.wasFailure(DomainError(PageDomainErrorCode.NO_SUCH_PAGE_WITH_THIS_ID.name))
        }

        var deletedPage = page.get().deletePage(callingUser)

        if(deletedPage.wasFailure()){
            return Either.wasFailure(deletedPage.fail!!)
        }

        return Either.wasSuccess(pages.trigger(deletedPage.success!!));
    }
}