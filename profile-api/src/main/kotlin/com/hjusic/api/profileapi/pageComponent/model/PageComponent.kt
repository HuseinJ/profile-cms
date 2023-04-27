package com.hjusic.api.profileapi.pageComponent.model

import java.util.UUID

class PageComponent(
    val id: UUID,
    val componentName: PageComponentName,
    val componentData: Map<String, String>
){
}