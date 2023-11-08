package com.hjusic.api.profileapi.pageComponent.model

import com.hjusic.api.profileapi.page.model.Page

class PageComponentSwitched(
    firstPageComponent: PageComponent,
    val secondPageComponent: PageComponent,
    page: Page) :
    PageComponentEvent(firstPageComponent, page)