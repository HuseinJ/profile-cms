package com.hjusic.api.profileapi.pageComponent.application

import com.hjusic.api.profileapi.common.error.ContextError
import com.hjusic.api.profileapi.common.error.ValidationError
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.common.result.Either
import com.hjusic.api.profileapi.page.model.Pages
import com.hjusic.api.profileapi.pageComponent.model.PageComponent
import com.hjusic.api.profileapi.pageComponent.model.PageComponents
import com.hjusic.api.profileapi.user.model.User
import java.util.*
import kotlin.collections.HashMap

class SetComponentDataOnPageComponent(
    var pageComponents: PageComponents,
    var pages: Pages
) {

    fun setComponentDataOnPageComponent(
        componentDataGraphQlInput: List<Map<String, String>>,
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

        var potentialPage = pages.findPageById(pageId)

        if(potentialPage.isEmpty){
            return Either.wasFailure(ValidationError(ValidationErrorCode.EMPTY_VALUE))
        }
        var pageComponent = pageComponents.findComponentsOfPage(pageId, componentID)

        val componentDataMap = HashMap<String, String>()
        componentDataGraphQlInput.forEach { item -> componentDataMap.put(item.get("key")!!, item.get("value")!!) }

        val result = pageComponent.setComponentData( componentDataMap, callingUser, potentialPage.get())

        if(result.wasFailure()){
            return Either.wasFailure(result.fail!!)
        }

        return Either.wasSuccess(pageComponents.trigger(result.success!!))
    }
}