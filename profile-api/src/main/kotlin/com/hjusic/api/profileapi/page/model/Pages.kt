package com.hjusic.api.profileapi.page.model

import java.util.*

interface Pages {
    fun trigger(pageEvent: PageEvent): Page;
    fun findPageById(uuid: UUID): Optional<Page>;
    fun findAll(): Collection<Page>;
}