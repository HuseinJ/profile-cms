package com.hjusic.api.profileapi.page.model

import java.util.*

class HomePageAssigned private constructor(
   page: Page
): PageEvent(page) {

    companion object{
        fun now(page: Page): HomePageAssigned {
            return HomePageAssigned(page);
        }
    }
}