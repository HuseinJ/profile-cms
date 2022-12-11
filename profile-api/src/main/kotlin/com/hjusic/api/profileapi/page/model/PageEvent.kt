package com.hjusic.api.profileapi.page.model

import com.hjusic.api.profileapi.common.event.Event
import java.time.Instant

abstract class PageEvent internal constructor(
    page: Page
): Event(page.id, Instant.now()) {

}