package com.hjusic.api.profileapi.page.graphql

import com.hjusic.api.profileapi.common.security.UserAuthServices
import com.hjusic.api.profileapi.page.application.CreatePage
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import org.springframework.security.access.annotation.Secured

@DgsComponent
class CreatePageGraphQlService(
    var userAuthServices: UserAuthServices,
    var createPage: CreatePage,
) {

    @DgsMutation
    @Secured
    fun createPage(@InputArgument("name") name: String): PageGraphQlView{
        var loggedInUser = userAuthServices.callingUser()

        var result = createPage.createPage(name, loggedInUser)

        if(result.wasFailure()){
            throw SecurityException(result.fail?.reason);
        }

        return PageGraphQlView.from(result.success!!);
    }
}