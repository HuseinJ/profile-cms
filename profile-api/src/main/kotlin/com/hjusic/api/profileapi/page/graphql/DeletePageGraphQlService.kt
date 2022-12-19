package com.hjusic.api.profileapi.page.graphql

import com.hjusic.api.profileapi.common.security.UserAuthServices
import com.hjusic.api.profileapi.page.application.DeletePage
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import org.springframework.security.access.annotation.Secured

@DgsComponent
class DeletePageGraphQlService(
    var userAuthServices: UserAuthServices,
    var deletePage: DeletePage
) {

    @DgsMutation
    @Secured
    fun deletePage(@InputArgument("id") id: String): PageGraphQlView{
        var loggedInUser = userAuthServices.callingUser()
        var result = deletePage.deletePage(id, loggedInUser)

        if(result.wasFailure()){
            throw SecurityException(result.fail?.reason);
        }

        return PageGraphQlView.from(result.success!!);
    }
}