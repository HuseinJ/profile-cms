package com.hjusic.api.profileapi.user.graphql

import com.hjusic.api.profileapi.common.security.UserAuthServices
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import org.springframework.security.access.annotation.Secured

@DgsComponent
class LoggedInUserGraphQlService(
    val userAuthServices: UserAuthServices
) {

    @Secured
    @DgsQuery
    fun loggedInUser(): UserGraphQlView? {
        var loggedInUser = userAuthServices?.callingUser()

        if(loggedInUser == null){
            throw SecurityException("No user logged in");
        }
        return UserGraphQlView.from(loggedInUser)
    }
}