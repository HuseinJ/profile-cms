package com.hjusic.api.profileapi.pageComponent.graphql

import com.hjusic.api.profileapi.common.security.UserAuthServices
import com.hjusic.api.profileapi.pageComponent.application.SetComponentDataOnPageComponent
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import org.springframework.security.access.annotation.Secured

@DgsComponent
class SetPageComponentDataGraphQlService(
    val userAuthServices: UserAuthServices,
    val setComponentDataOnPageComponent: SetComponentDataOnPageComponent
) {

    @DgsMutation
    @Secured
    fun setPageComponentData(
        @InputArgument componentData: List<ComponentDataGraphQlInput>,
        @InputArgument pageComponentId: String,
        @InputArgument pageId: String
    ): PageComponentGraphQlView {
        var loggedInUser = userAuthServices.callingUser()

        var result = setComponentDataOnPageComponent.setComponentDataOnPageComponent(componentData, pageId, pageComponentId, loggedInUser)

        if (result.wasFailure()) {
            throw SecurityException(result.fail?.reason)
        }

        return PageComponentGraphQlView.from(result.success!!)
    }
}