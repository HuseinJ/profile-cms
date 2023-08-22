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

    fun removeComponentById(componentId: UUID){
        components.removeIf { component -> component.id == componentId }
    }

    fun getComponents(): List<PageComponentDatabaseEntity> {
        return Collections.unmodifiableList(components)
    }
}