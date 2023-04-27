package com.hjusic.api.profileapi.pageComponent.infrastucture

import org.springframework.data.annotation.Id
import java.util.*
import kotlin.collections.HashMap

class PageComponentDatabaseEntity(

    @Id
    val id: UUID = UUID.randomUUID(),
    val componentName: String,
    val componentData: Map<String, String> = HashMap()
)