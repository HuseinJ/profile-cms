package com.hjusic.api.profileapi.pageComponent.graphql

import com.hjusic.api.profileapi.pageComponent.model.PageComponent
import com.hjusic.api.profileapi.pageComponent.model.PageComponentName

class PageComponentGraphQlView private constructor(
    val id: String,
    val name: PageComponentName,
    val componentData: List<ComponentData>,
    val pageid: String,
) {

    companion object {
        fun from(pageComponent: PageComponent): PageComponentGraphQlView {

            var componentData =
                pageComponent.componentData.entries.stream().map { entry -> ComponentData(entry.key, entry.value) }
                    .toList()

            return PageComponentGraphQlView(
                pageComponent.id.toString(),
                pageComponent.componentName,
                componentData,
                pageComponent.getPageId().toString()
            )
        }
    }

    class ComponentData(
        val key: String,
        val value: String,
    )
}