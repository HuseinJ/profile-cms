package com.hjusic.api.profileapi.page.model

import java.util.UUID

interface Pages {
    fun trigger(pageEvent: PageEvent): Page;
    fun findPageById(uuid: UUID): Page;
}