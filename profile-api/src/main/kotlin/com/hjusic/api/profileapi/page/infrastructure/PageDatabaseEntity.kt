package com.hjusic.api.profileapi.page.infrastructure

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
class PageDatabaseEntity(
    @Id
    val id: UUID = UUID.randomUUID(),
    val pageType: PageEntityType,
    val name: String,
) {
}