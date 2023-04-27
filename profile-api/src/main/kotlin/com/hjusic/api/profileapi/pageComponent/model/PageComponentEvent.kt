package com.hjusic.api.profileapi.pageComponent.model

import com.hjusic.api.profileapi.common.event.Event
import com.hjusic.api.profileapi.page.model.Page
import java.time.Instant

abstract class PageComponentEvent internal constructor(
    val pageComponent: PageComponent,
    val page: Page
): Event(pageComponent.id, Instant.now()) {
}