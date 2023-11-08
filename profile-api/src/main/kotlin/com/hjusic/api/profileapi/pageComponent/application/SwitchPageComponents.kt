package com.hjusic.api.profileapi.pageComponent.application

import com.hjusic.api.profileapi.common.error.ContextError
import com.hjusic.api.profileapi.common.error.ValidationError
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.common.result.Either
import com.hjusic.api.profileapi.page.model.Page
import com.hjusic.api.profileapi.page.model.Pages
import com.hjusic.api.profileapi.pageComponent.model.PageComponents
import com.hjusic.api.profileapi.user.model.User
import java.util.*

class SwitchPageComponents(
    var pageComponents: PageComponents,
    var pages: Pages
) {

    fun switchPageComponents(
        firstPageComponentId: String,
        secondPageComponentId: String,
        possiblePageId: String,
        callingUser: User
    ): Either<ContextError, Page> {
        if (possiblePageId == "" ||
            firstPageComponentId == "" ||
            secondPageComponentId == "") return Either.wasFailure(ValidationError(ValidationErrorCode.EMPTY_VALUE))

        var pageId: UUID
        var firstCPId: UUID
        var secondCPId: UUID

        try {
            pageId = UUID.fromString(possiblePageId)
            firstCPId = UUID.fromString(firstPageComponentId)
            secondCPId = UUID.fromString(secondPageComponentId)
        } catch (e: IllegalArgumentException) {
            return Either.wasFailure(ValidationError(ValidationErrorCode.WRONG_FORMAT))
        }

        var possiblePage = pages.findPageById(pageId)
        if (possiblePage.isEmpty) {
            return Either.wasFailure(ValidationError(ValidationErrorCode.EMPTY_VALUE))
        }
        var page = possiblePage.get()

        var firstPageComponent = pageComponents.findComponentsOfPage(page.id, firstCPId)
        var secondPageComponent = pageComponents.findComponentsOfPage(page.id, secondCPId)

        var possibleEvent = page.switchComponents(firstPageComponent, secondPageComponent, callingUser)

        if(possibleEvent.wasFailure()){
            return Either.wasFailure(possibleEvent.fail!!);
        }

        pageComponents.trigger(possibleEvent.success!!)
        return Either.wasSuccess(pages.findPageById(pageId).get())
    }
}



