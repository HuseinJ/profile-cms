package com.hjusic.api.profileapi.pageComponent.application

import com.hjusic.api.profileapi.common.error.ContextError
import com.hjusic.api.profileapi.common.error.ValidationError
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.common.result.Either
import com.hjusic.api.profileapi.page.model.Pages
import com.hjusic.api.profileapi.pageComponent.model.PageComponent
import com.hjusic.api.profileapi.pageComponent.model.PageComponents
import com.hjusic.api.profileapi.user.model.User
import java.rmi.server.UID
import java.util.*

class RemovePageComponent(
    var pageComponents: PageComponents,
    var pages: Pages
) {

    fun removePageComponent(possiblePageId: String, possiblePageComponentId: String, callingUser: User): Either<ContextError, PageComponent> {
        if (possiblePageId == "" || possiblePageComponentId == "") {
            return Either.wasFailure(ValidationError(ValidationErrorCode.EMPTY_VALUE))
        }
        var pageId: UUID
        var componentID: UUID
        try {
            pageId = UUID.fromString(possiblePageId)
            componentID = UUID.fromString(possiblePageComponentId)
        } catch (e: IllegalArgumentException) {
            return Either.wasFailure(ValidationError(ValidationErrorCode.WRONG_FORMAT))
        }

        var pageComponent = pageComponents.findComponentsOfPage(pageId, componentID)
        pageComponent.setPageId(pageId)
        var possibleRemoveEvent = pageComponent.removePageComponent(callingUser)

        if(possibleRemoveEvent.wasFailure()){
            return Either.wasFailure(possibleRemoveEvent.fail!!)
        }

        return Either.wasSuccess(pageComponents.trigger(possibleRemoveEvent.success!!))

    }
}