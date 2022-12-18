package com.hjusic.api.profileapi.page.infrastructure

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.UUID

interface PageDatabaseEntityRepository: MongoRepository<PageDatabaseEntity, UUID> {
}