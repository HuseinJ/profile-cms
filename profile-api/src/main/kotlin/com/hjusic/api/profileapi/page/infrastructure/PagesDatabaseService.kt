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
            is PageDeleted -> page = handle(pageEvent)
            else -> {
                throw java.lang.IllegalArgumentException("Unsupported argument")
            }
        }

        eventPublisher.publish(pageEvent)

        return page
    }

    override fun findPageById(uuid: UUID): Optional<Page> {
        var page = pageDatabaseEntityRepository.findById(uuid);

        if(page.isEmpty){
            return Optional.empty()
        }
        return Optional.of(map(page.get()))
    }

    override fun findPageByName(name: String): Optional<Page> {
        var page = pageDatabaseEntityRepository.findByName(name);

        if(page.isEmpty){
            return Optional.empty()
        }
        return Optional.of(map(page.get()))
    }

    override fun findAll(): Collection<Page> {
        return pageDatabaseEntityRepository.findAll().stream().map { page -> map(page) }.toList();
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

    private fun handle(pageDeleted: PageDeleted): Page {
        pageDatabaseEntityRepository.deleteById(pageDeleted.page.id)

        return pageDeleted.page
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