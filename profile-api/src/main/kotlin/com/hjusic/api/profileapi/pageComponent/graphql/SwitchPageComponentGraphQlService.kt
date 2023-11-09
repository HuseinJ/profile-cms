package com.hjusic.api.profileapi.pageComponent.graphql

import com.hjusic.api.profileapi.common.security.UserAuthServices
import com.hjusic.api.profileapi.page.graphql.PageGraphQlView
import com.hjusic.api.profileapi.pageComponent.application.SwitchPageComponents
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import org.springframework.security.access.annotation.Secured

@DgsComponent
class SwitchPageComponentGraphQlService(
    val userAuthServices: UserAuthServices,
    val switchPageComponents: SwitchPageComponents
) {
    @DgsMutation
    @Secured
    fun switchComponents(pageId: String, firstComponentId: String, secondComponentId: String): PageGraphQlView{
        var loggedInUser = userAuthServices.callingUser()

        var result = switchPageComponents.switchPageComponents(firstComponentId, secondComponentId, pageId, loggedInUser)

        if (result.wasFailure()) {
            throw SecurityException(result.fail?.reason)
        }

        return PageGraphQlView.from(result.success!!)
    }
}