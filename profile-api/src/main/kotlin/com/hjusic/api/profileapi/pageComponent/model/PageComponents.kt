package com.hjusic.api.profileapi.pageComponent.model

import java.util.UUID

interface PageComponents {

    fun findComponentsOfPage(uuid: UUID): List<PageComponent>
    fun trigger(pageComponentEvent: PageComponentEvent): PageComponent;
}