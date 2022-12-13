package com.hjusic.api.profileapi.user.graphql

import com.hjusic.api.profileapi.user.application.SignInUser
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class SignInGraphQlService(
    val signInUser: SignInUser
) {

    @DgsMutation
    fun signIn(@InputArgument("name") name: String, @InputArgument("password") password: String): LoggedInUserGraphQlView{
        var result = signInUser.signInUser(name, password);

        if(result.wasFailure()){
            throw SecurityException(result.fail?.reason);
        }

        return LoggedInUserGraphQlView.from(result.success!!);
    }

}