package com.hjusic.api.profileapi.pageComponent.graphql

import com.hjusic.api.profileapi.common.security.UserAuthServices
import com.hjusic.api.profileapi.pageComponent.application.RemovePageComponent
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import org.springframework.security.access.annotation.Secured

@DgsComponent
class RemovePageComponentGraphQlView (
    val userAuthServices: UserAuthServices, val removePageComponent: RemovePageComponent
        ) {

    @DgsMutation
    @Secured
    fun removePageComponent(
        @InputArgument("pageComponentId") pageComponentId: String,
        @InputArgument("pageId") pageId: String
    ): PageComponentGraphQlView {

        var loggedInUser = userAuthServices.callingUser()

        var result = removePageComponent.removePageComponent(pageId, pageComponentId, loggedInUser)

        if (result.wasFailure()) {
            throw SecurityException(result.fail?.reason)
        }

        return PageComponentGraphQlView.from(result.success!!)
    }
}