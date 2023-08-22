package com.hjusic.api.profileapi.page.infrastructure

import com.hjusic.api.profileapi.page.model.Page
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface PageDatabaseEntityRepository: MongoRepository<PageDatabaseEntity, UUID> {
    fun findByName(name: String): Optional<PageDatabaseEntity>
    fun findByPageType(pageEntityType: PageEntityType): List<PageDatabaseEntity>
}