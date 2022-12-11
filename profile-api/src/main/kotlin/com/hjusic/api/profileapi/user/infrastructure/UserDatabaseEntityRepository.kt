package com.hjusic.api.profileapi.user.infrastructure

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface UserDatabaseEntityRepository: MongoRepository<UserDatabaseEntity, UUID> {
    fun findByName(name: String): Optional<UserDatabaseEntity>
    fun existsByName(username: String?): Boolean
    fun existsByEmail(email: String?): Boolean
}