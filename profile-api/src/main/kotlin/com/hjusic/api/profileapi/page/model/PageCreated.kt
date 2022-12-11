package com.hjusic.api.profileapi.page.model

import java.util.*

class PageCreated private constructor(
    name: String
): PageEvent(UnpublishedPage(UUID.randomUUID(),name)) {

    companion object{
        fun now(name: String): PageCreated {
            return PageCreated(name);
        }
    }
}