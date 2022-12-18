package com.hjusic.api.profileapi.page.model

interface Pages {
    fun trigger(pageEvent: PageEvent): Page;
}