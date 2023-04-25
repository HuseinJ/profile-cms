package com.hjusic.api.profileapi.user.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class RefreshTokenGraphQlService{

    @DgsQuery
    fun refreshToken(@InputArgument("refreshToken") refreshToken: String){
        val requestRefreshToken: String = refreshToken
    }
}