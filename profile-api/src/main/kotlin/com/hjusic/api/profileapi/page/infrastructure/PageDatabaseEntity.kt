package com.hjusic.api.profileapi.page.infrastructure

import com.hjusic.api.profileapi.pageComponent.infrastucture.PageComponentDatabaseEntity
import com.hjusic.api.profileapi.pageComponent.model.PageComponent
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import kotlin.collections.ArrayList

@Document
class PageDatabaseEntity(
    @Id
    val id: UUID = UUID.randomUUID(),
    var pageType: PageEntityType,
    val name: String,
    private val components: MutableList<PageComponentDatabaseEntity> = ArrayList()
) {

    fun addComponent(pageComponentDatabaseEntity: PageComponentDatabaseEntity){
        components.add(pageComponentDatabaseEntity)
    }

    fun removeComponentById(componentId: UUID) {
        val componentToRemove = components.firstOrNull { it.id == componentId }
        if (componentToRemove != null) {
            val orderToRemove = componentToRemove.order
            components.removeIf { it.id == componentId }

            // Decrement the order of components with a higher order
            components.forEach { component ->
                if (component.order > orderToRemove) {
                    component.order--
                }
            }
        }
    }

    fun getComponents(): List<PageComponentDatabaseEntity> {
        return Collections.unmodifiableList(components)
    }
}