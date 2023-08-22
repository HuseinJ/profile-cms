package com.hjusic.api.profileapi.page.application

import com.hjusic.api.profileapi.common.error.ContextError
import com.hjusic.api.profileapi.common.error.ValidationError
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.common.result.Either
import com.hjusic.api.profileapi.page.model.*
import com.hjusic.api.profileapi.user.model.User
import java.util.*

class AssignHomePage(
    var pages: Pages,
) {
    fun assignHomePage(id: String, callingUser: User): Either<ContextError, Page> {
        if (id == "") {
            return Either.wasFailure(ValidationError(ValidationErrorCode.EMPTY_VALUE))
        }

        val potentialUUID = try {
            UUID.fromString(id)
        } catch (e: IllegalArgumentException) {
            // Handle the case where the provided id is not a valid UUID
            return Either.wasFailure(ValidationError(ValidationErrorCode.WRONG_FORMAT))
        }

        val potentialPage = pages.findPageById(potentialUUID)

        val page = potentialPage.get()

        if (page is UnpublishedPage) {
            var potentialPageAssignEvent = page.assignHomePage(callingUser)

            if(potentialPageAssignEvent.wasFailure()) {
                return Either.wasFailure(potentialPageAssignEvent.fail!!)
            }

            return Either.wasSuccess(pages.trigger(potentialPageAssignEvent.success!!))
        }
        return Either.wasFailure(ValidationError(ValidationErrorCode.ACTION_NOT_ALLOWED_FOR_OBJECT))
    }
}