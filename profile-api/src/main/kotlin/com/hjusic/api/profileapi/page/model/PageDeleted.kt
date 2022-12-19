package com.hjusic.api.profileapi.page.model

class PageDeleted private constructor(
    val page: Page
): PageEvent(page) {

    companion object{
        fun now(page: Page): PageDeleted {
            return PageDeleted(page);
        }
    }
}