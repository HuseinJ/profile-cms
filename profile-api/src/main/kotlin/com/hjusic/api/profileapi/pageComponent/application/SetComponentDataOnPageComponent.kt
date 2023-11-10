package com.hjusic.api.profileapi.pageComponent.application

import com.hjusic.api.profileapi.common.error.ContextError
import com.hjusic.api.profileapi.common.error.ValidationError
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.common.result.Either
import com.hjusic.api.profileapi.page.model.Pages
import com.hjusic.api.profileapi.pageComponent.graphql.ComponentDataGraphQlInput
import com.hjusic.api.profileapi.pageComponent.model.PageComponent
import com.hjusic.api.profileapi.pageComponent.model.PageComponents
import com.hjusic.api.profileapi.user.model.User
import java.util.*

class SetComponentDataOnPageComponent(
    var pageComponents: PageComponents,
    var pages: Pages
) {

    fun setComponentDataOnPageComponent(
        componentDataGraphQlInput: List<ComponentDataGraphQlInput>,
        possiblePageId: String,
        possiblePageComponentId: String,
        callingUser: User): Either<ContextError, PageComponent> {
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

        return Either.wasFailure(ContextError("not implemented"))
    }
}