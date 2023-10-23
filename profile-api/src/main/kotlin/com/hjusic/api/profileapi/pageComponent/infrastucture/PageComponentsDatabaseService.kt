package com.hjusic.api.profileapi.pageComponent.infrastucture

import com.hjusic.api.profileapi.common.event.EventPublisher
import com.hjusic.api.profileapi.page.infrastructure.PageDatabaseEntity
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

        return possiblePage.get().getComponents().stream()
            .map { component ->
                val mappedComponent = map(component)
                mappedComponent.pageid = uuid
                mappedComponent
            }
            .toList()
    }

    override fun findComponentsOfPage(pageId: UUID, comonentId: UUID): PageComponent {
        var possiblePage = pageDatabaseEntityRepository.findById(pageId)

        if (possiblePage.isEmpty) {
            throw java.lang.IllegalArgumentException("given page is not valid")
        }

        var pageComponent = possiblePage.get().getComponents().find { component -> component.id == comonentId }

        if (pageComponent == null) {
            throw java.lang.IllegalArgumentException("given pageComponent is not valid")
        }

        return map(pageComponent)
    }

    override fun trigger(pageComponentEvent: PageComponentEvent): PageComponent {
        val pageComponent = when (pageComponentEvent) {
            is PageComponentAdded -> handle(pageComponentEvent)
            is PageComponentRemoved -> handle(pageComponentEvent)
            else -> {
                throw java.lang.IllegalArgumentException("Unsupported argument")
            }
        }

        pageComponent.pageid = pageComponentEvent.page.id

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
                findHighestOrder(page) + 1
            )
        )

        pageDatabaseEntityRepository.save(page)

        return pageComponentEvent.pageComponent
    }

    fun map(pageComponentDatabaseEntity: PageComponentDatabaseEntity): PageComponent {
        var pageComponent = PageComponent(
            pageComponentDatabaseEntity.id,
            PageComponentName.valueOf(pageComponentDatabaseEntity.componentName),
            pageComponentDatabaseEntity.componentData,
        )

        pageComponent.order = pageComponentDatabaseEntity.order

        return pageComponent
    }

    private fun findHighestOrder(page: PageDatabaseEntity): Int {
        return page.getComponents().stream()
            .mapToInt { pc -> pc.order }
            .max()
            .orElse(0)
    }
}