package com.hjusic.api.profileapi.file.infrastructure

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface FileDatabaseEntityRepository: MongoRepository<FileDatabaseEntity, UUID> {
}

