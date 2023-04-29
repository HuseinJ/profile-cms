package com.hjusic.api.profileapi.pageComponent.application

import com.hjusic.api.profileapi.common.error.ContextError
import com.hjusic.api.profileapi.common.result.Either
import com.hjusic.api.profileapi.page.model.Pages
import com.hjusic.api.profileapi.pageComponent.model.PageComponent
import com.hjusic.api.profileapi.pageComponent.model.PageComponents

class RemovePageComponent(
    var pageComponents: PageComponents,
    var pages: Pages
) {

    fun createPageComponent(): Either<ContextError, PageComponent> {


    }
}