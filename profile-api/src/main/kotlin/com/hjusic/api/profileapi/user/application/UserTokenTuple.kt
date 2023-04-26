package com.hjusic.api.profileapi.user.application

import com.hjusic.api.profileapi.user.model.User

class UserTokenTuple(
    val user: User,
    val token: String,
    val refreshToken: String?,
) {
}