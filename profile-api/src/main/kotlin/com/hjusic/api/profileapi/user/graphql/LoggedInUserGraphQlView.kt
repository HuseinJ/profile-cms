package com.hjusic.api.profileapi.user.graphql

import com.hjusic.api.profileapi.user.application.UserTokenTuple

class LoggedInUserGraphQlView private constructor(
    val token: String, val refreshToken: String, val type: String, val user: UserGraphQlView
) {

    companion object {

        fun from(userTokenTuple: UserTokenTuple): LoggedInUserGraphQlView {
            return LoggedInUserGraphQlView(userTokenTuple.token, "","Bearer",UserGraphQlView.from(userTokenTuple.user))
        }
    }
}