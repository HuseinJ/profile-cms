package com.hjusic.api.profileapi.pageComponent.graphql

import com.hjusic.api.profileapi.page.model.Page
import com.hjusic.api.profileapi.pageComponent.model.PageComponents
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment


@DgsComponent
class PageComponentGraphQlService(
    val pageComponents: PageComponents
) {

    @DgsData(parentType = "Page", field = "pageComponents")
    fun reviews(dfe: DgsDataFetchingEnvironment): List<PageComponentGraphQlView> {
        val page: Page = dfe.getSource()

        return pageComponents.findComponentsOfPage(page.id).stream()
            .map { component -> PageComponentGraphQlView.from(component) }.toList()
    }
}