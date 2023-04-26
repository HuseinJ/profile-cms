package com.hjusic.api.profileapi.user.infrastructure

import org.springframework.data.mongodb.repository.MongoRepository
interface RefreshTokenDatabaseEntityRepository: MongoRepository<RefreshTokenDatabaseEntity, String> {
}