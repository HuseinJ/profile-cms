package com.hjusic.api.profileapi.user.model

import com.hjusic.api.profileapi.accessRole.model.AccessRole
import java.time.Instant
import java.util.*

class User (
    var id: UUID,
    var name: String,
    var email: String,
    var roles: Set<AccessRole>,
    var refreshToken: RefreshToken?
    ) {

    fun changePassword(newEncryptedPassword: String): PasswordChanged{
        return PasswordChanged.from(this, newEncryptedPassword)
    }

    fun createRefreshToken(token: String, expirationDate: Instant): RefreshTokenCreated{
        return RefreshTokenCreated(this, RefreshToken(UUID.randomUUID(), token, expirationDate))
    }

}