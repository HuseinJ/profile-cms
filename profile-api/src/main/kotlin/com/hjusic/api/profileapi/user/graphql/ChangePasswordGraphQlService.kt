package com.hjusic.api.profileapi.user.graphql

import com.hjusic.api.profileapi.common.security.UserAuthServices
import com.hjusic.api.profileapi.user.application.ChangePassword
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import org.springframework.security.access.annotation.Secured

@DgsComponent
class ChangePasswordGraphQlService(
    val changePassword: ChangePassword,
    val userAuthServices: UserAuthServices
) {

    @DgsMutation
    @Secured
    fun changePassword(@InputArgument("newPassword") newPassword: String, @InputArgument("oldPassword") oldPassword: String): Boolean{
        var loggedInUser = userAuthServices?.callingUser()

        var result = changePassword.changePassword(loggedInUser, oldPassword, newPassword)

        if(result.wasFailure()){
            throw SecurityException(result.fail?.reason);
        }

        return result.wasSuccess();
    }
}