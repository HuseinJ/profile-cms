package com.hjusic.api.profileapi.user.graphql

import com.hjusic.api.profileapi.user.model.Users
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import org.springframework.security.access.annotation.Secured

@DgsComponent
class UsersGraphQlService(
    val users: Users
) {

    @DgsQuery
    @Secured("ROLE_ADMIN")
    fun users(): List<UserGraphQlView>? {
        return users.findAll().stream().map { user -> UserGraphQlView.from(user) }.toList()
    }
}