package com.hjusic.api.profileapi.user.infrastructure

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.*

@Document
class RefreshTokenDatabaseEntity(
    @Id
    val id: UUID,
    val token: String,
    val expiryDate: Instant
)