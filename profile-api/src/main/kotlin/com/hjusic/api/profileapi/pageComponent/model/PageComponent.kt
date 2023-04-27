package com.hjusic.api.profileapi.pageComponent.model

import java.util.UUID

class PageComponent(
    val id: UUID,
    val componentName: PageComponentName,
    val componentData: Map<String, String>,
    private var pageid: UUID?
){
    fun getPageId(): UUID? {
        return pageid
    }
    fun setPageId(pageid: UUID): PageComponent {
        this.pageid = pageid
        return this
    }

}