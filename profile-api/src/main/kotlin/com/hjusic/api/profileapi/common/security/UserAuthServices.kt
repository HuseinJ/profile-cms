package com.hjusic.api.profileapi.common.security

import com.hjusic.api.profileapi.user.model.User

interface UserAuthServices {
    fun callingUser(): User
}