package com.hjusic.api.profileapi.pageComponent.model

import com.hjusic.api.profileapi.page.model.Page

class PageComponentDataSet(
    pageComponent: PageComponent,
    page: Page,
    val componentData: Map<String, String>) : PageComponentEvent(pageComponent, page)