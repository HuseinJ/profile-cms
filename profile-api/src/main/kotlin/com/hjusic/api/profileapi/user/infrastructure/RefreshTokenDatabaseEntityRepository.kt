package com.hjusic.api.profileapi.user.infrastructure

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface RefreshTokenDatabaseEntityRepository: MongoRepository<RefreshTokenDatabaseEntity, UUID> {
}