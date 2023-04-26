package com.hjusic.api.profileapi.user.model

import com.hjusic.api.profileapi.user.infrastructure.RefreshTokenDatabaseEntity
import java.time.Instant
import java.util.UUID

class RefreshToken(
    val token: String,
    val expirationDate: Instant
) {

    companion object{
        fun from(refreshToken: RefreshTokenDatabaseEntity):RefreshToken{
            return RefreshToken(refreshToken.token, refreshToken.expiryDate)
        }
    }
}