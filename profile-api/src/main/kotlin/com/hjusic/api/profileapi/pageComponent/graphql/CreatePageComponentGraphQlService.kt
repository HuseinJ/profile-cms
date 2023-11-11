package com.hjusic.api.profileapi.pageComponent.graphql

import com.hjusic.api.profileapi.common.security.UserAuthServices
import com.hjusic.api.profileapi.pageComponent.application.CreatePageComponent
import com.hjusic.api.profileapi.pageComponent.model.PageComponentName
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import org.springframework.security.access.annotation.Secured

@DgsComponent
class CreatePageComponentGraphQlService(
    val userAuthServices: UserAuthServices, val createPageComponent: CreatePageComponent
) {

    @DgsMutation
    @Secured
    fun createPageComponent(
        @InputArgument("name") name: PageComponentName,
        @InputArgument("pageId") pageId: String
    ): PageComponentGraphQlView {
        var loggedInUser = userAuthServices.callingUser()

        var result = createPageComponent.createPageComponent(name, pageId, loggedInUser)

        if (result.wasFailure()) {
            throw SecurityException(result.fail?.reason)
        }

        return PageComponentGraphQlView.from(result.success!!)
    }
}