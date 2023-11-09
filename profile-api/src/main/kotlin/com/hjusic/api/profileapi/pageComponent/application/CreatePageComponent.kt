package com.hjusic.api.profileapi.pageComponent.application

import com.hjusic.api.profileapi.common.error.ContextError
import com.hjusic.api.profileapi.common.error.ValidationError
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.common.result.Either
import com.hjusic.api.profileapi.page.model.Pages
import com.hjusic.api.profileapi.pageComponent.model.PageComponent
import com.hjusic.api.profileapi.pageComponent.model.PageComponentName
import com.hjusic.api.profileapi.pageComponent.model.PageComponents
import com.hjusic.api.profileapi.user.model.User
import java.util.*

class CreatePageComponent(
    var pageComponents: PageComponents,
    var pages: Pages
) {

    fun createPageComponent(
        pageComponentName: PageComponentName,
        possiblePageId: String,
        callingUser: User
    ): Either<ContextError, PageComponent> {
        if (possiblePageId == "") {
            return Either.wasFailure(ValidationError(ValidationErrorCode.EMPTY_VALUE))
        }
        var pageId: UUID

        try {
            pageId = UUID.fromString(possiblePageId)
        } catch (e: IllegalArgumentException) {
            return Either.wasFailure(ValidationError(ValidationErrorCode.WRONG_FORMAT))
        }

        var possiblePage = pages.findPageById(pageId)

        if (possiblePage.isEmpty) {
            return Either.wasFailure(ValidationError(ValidationErrorCode.EMPTY_VALUE))
        }

        var createPageComponentEvent = possiblePage.get().addComponent(
            PageComponent(
                UUID.randomUUID(),
                pageComponentName,
                HashMap(),
            ),
            callingUser
        )

        if(createPageComponentEvent.wasFailure()){
            return Either.wasFailure(createPageComponentEvent.fail!!)
        }

        return Either.wasSuccess(pageComponents.trigger(createPageComponentEvent.success!!))
    }
}