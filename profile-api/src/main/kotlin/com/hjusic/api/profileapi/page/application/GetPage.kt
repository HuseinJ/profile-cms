package com.hjusic.api.profileapi.page.application

import com.hjusic.api.profileapi.common.error.ValidationError
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.common.result.Either
import com.hjusic.api.profileapi.page.model.Page
import com.hjusic.api.profileapi.page.model.Pages

import java.util.*


class GetPage(
    var pages: Pages,
) {

    fun getPage(uuid: String?, name: String?): Either<ValidationError, Optional<Page>> {
        try {
            if(uuid != null) {
                var possibleUuid = UUID.fromString(uuid);
                return Either.wasSuccess(pages.findPageById(possibleUuid))
            } else if (name != null) {
                return Either.wasSuccess(pages.findPageByName(name))
            }
            return Either.wasFailure(ValidationError(ValidationErrorCode.EMPTY_VALUE))
        }catch (e: Exception){
            return Either.wasFailure(ValidationError(ValidationErrorCode.WRONG_FORMAT))
        }
    }
}