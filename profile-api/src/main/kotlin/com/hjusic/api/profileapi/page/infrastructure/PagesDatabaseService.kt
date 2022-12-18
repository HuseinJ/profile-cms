package com.hjusic.api.profileapi.page.infrastructure

import com.hjusic.api.profileapi.common.event.EventPublisher
import com.hjusic.api.profileapi.page.model.*
import java.util.*

class PagesDatabaseService(
    private val eventPublisher: EventPublisher, private val pageDatabaseEntityRepository: PageDatabaseEntityRepository
) : Pages {

    override fun trigger(pageEvent: PageEvent): Page {
        val page: Page

        when (pageEvent) {
            is PageCreated -> page = handle(pageEvent)
            else -> {
                throw java.lang.IllegalArgumentException("Unsupported argument")
            }
        }

        eventPublisher.publish(pageEvent)

        return page
    }

    private fun handle(pageCreated: PageCreated): Page {
        return map(
            pageDatabaseEntityRepository.save(
                PageDatabaseEntity(
                    UUID.randomUUID(), PageEntityType.UNPUBLISHED, pageCreated.name
                )
            )
        )

    }

    fun map(pageDatabaseEntity: PageDatabaseEntity): Page {
        return when (pageDatabaseEntity.pageType) {
            PageEntityType.UNPUBLISHED -> UnpublishedPage(
                pageDatabaseEntity.id, pageDatabaseEntity.name
            );
            else -> throw java.lang.IllegalArgumentException("Unsupported argument")
        }
    }
}