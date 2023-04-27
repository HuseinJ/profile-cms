package com.hjusic.api.profileapi.pageComponent.infrastucture

import com.hjusic.api.profileapi.common.event.EventPublisher
import com.hjusic.api.profileapi.page.infrastructure.PageDatabaseEntityRepository
import com.hjusic.api.profileapi.pageComponent.model.*
import java.util.*

class PageComponentsDatabaseService(
    private val eventPublisher: EventPublisher, private val pageDatabaseEntityRepository: PageDatabaseEntityRepository
) : PageComponents {
    override fun findComponentsOfPage(uuid: UUID): List<PageComponent> {
        var possiblePage = pageDatabaseEntityRepository.findById(uuid)

        if (possiblePage.isEmpty) {
            throw java.lang.IllegalArgumentException("given page is not valid")
        }

        return possiblePage.get().getComponents().stream().map { component -> map(component).setPageId(uuid) }.toList()
    }

    override fun trigger(pageComponentEvent: PageComponentEvent): PageComponent {
        val pageComponent = when (pageComponentEvent) {
            is PageComponentAdded -> handle(pageComponentEvent)
            is PageComponentRemoved -> handle(pageComponentEvent)
            else -> {
                throw java.lang.IllegalArgumentException("Unsupported argument")
            }
        }

        pageComponent.setPageId(pageComponentEvent.page.id)

        eventPublisher.publish(pageComponentEvent)

        return pageComponent
    }

    private fun handle(pageComponentRemoved: PageComponentRemoved): PageComponent {
        var possiblePage = pageDatabaseEntityRepository.findById(pageComponentRemoved.page.id)

        if (possiblePage.isEmpty) {
            throw java.lang.IllegalArgumentException("given page is not valid")
        }

        var page = possiblePage.get()

        page.removeComponentById(pageComponentRemoved.pageComponent.id)

        pageDatabaseEntityRepository.save(page)

        return pageComponentRemoved.pageComponent
    }

    private fun handle(pageComponentEvent: PageComponentAdded): PageComponent {
        var possiblePage = pageDatabaseEntityRepository.findById(pageComponentEvent.page.id)

        if (possiblePage.isEmpty) {
            throw java.lang.IllegalArgumentException("given page is not valid")
        }

        var page = possiblePage.get()

        page.addComponent(
            PageComponentDatabaseEntity(
                pageComponentEvent.pageComponent.id,
                pageComponentEvent.pageComponent.componentName.name,
                pageComponentEvent.pageComponent.componentData,
            )
        )

        pageDatabaseEntityRepository.save(page)

        return pageComponentEvent.pageComponent
    }

    fun map(pageComponentDatabaseEntity: PageComponentDatabaseEntity): PageComponent {
        return PageComponent(
            pageComponentDatabaseEntity.id,
            PageComponentName.valueOf(pageComponentDatabaseEntity.componentName),
            pageComponentDatabaseEntity.componentData,
            null
        )
    }
}