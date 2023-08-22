package com.hjusic.api.profileapi.page.graphql

import com.hjusic.api.profileapi.common.security.UserAuthServices
import com.hjusic.api.profileapi.page.application.AssignHomePage
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import org.springframework.security.access.annotation.Secured

@DgsComponent
class AssignHomePageGraphQlService(
    var userAuthServices: UserAuthServices,
    var assignHomePage: AssignHomePage
) {

    @DgsMutation
    @Secured
    fun assignHomePage(@InputArgument("id") id: String): PageGraphQlView{
        var loggedInUser = userAuthServices.callingUser()

        var result = assignHomePage.assignHomePage(id, loggedInUser)

        if(result.wasFailure()){
            throw SecurityException(result.fail?.reason);
        }

        return PageGraphQlView.from(result.success!!);
    }
}