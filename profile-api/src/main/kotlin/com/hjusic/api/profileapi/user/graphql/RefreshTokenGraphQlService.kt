package com.hjusic.api.profileapi.user.graphql

import com.hjusic.api.profileapi.common.security.UserAuthServices
import com.hjusic.api.profileapi.user.application.RefreshTokenOfUser
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class RefreshTokenGraphQlService(
    var userAuthServices: UserAuthServices,
    val refreshTokenOfUser: RefreshTokenOfUser
){

    @DgsMutation
    fun refreshToken(@InputArgument("refreshToken") refreshToken: String): LoggedInUserGraphQlView{
        var result = refreshTokenOfUser.refreshTokenOfUser(refreshToken)

        if(result.wasFailure()){
            //TODO: Throw refreshToken Exception and handle it with redirect response or something
            throw SecurityException(result.fail?.reason);
        }

        return LoggedInUserGraphQlView.from(result.success!!);
    }
}