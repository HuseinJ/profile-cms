package com.hjusic.api.profileapi.page.application

import com.hjusic.api.profileapi.common.error.ContextError
import com.hjusic.api.profileapi.common.error.ValidationError
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.common.result.Either
import com.hjusic.api.profileapi.common.security.UserAuthServices
import com.hjusic.api.profileapi.page.model.CreatePageService
import com.hjusic.api.profileapi.page.model.Page
import com.hjusic.api.profileapi.page.model.Pages
import com.hjusic.api.profileapi.user.model.User


class CreatePage (
    var pages: Pages,
    var createPageService: CreatePageService,
    var userAuthServices: UserAuthServices
    ){

    fun createPage(name:String, callingUser: User): Either<ContextError, Page> {
        if(name == "") {
            return Either.wasFailure(ValidationError(ValidationErrorCode.EMPTY_VALUE))
        }

        var pageEvent = createPageService.createPage(callingUser, name);

        if(pageEvent.wasFailure()) {
            return Either.wasFailure(pageEvent.fail!!);
        }

        return Either.wasSuccess(pages.trigger(pageEvent.success!!))
    }
}