package com.hjusic.api.profileapi.pageComponent.graphql

import com.hjusic.api.profileapi.page.graphql.PageGraphQlView
import com.hjusic.api.profileapi.pageComponent.model.PageComponents
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import org.springframework.security.access.annotation.Secured
import java.util.*


@DgsComponent
class PageComponentGraphQlService(
    val pageComponents: PageComponents
) {
    @Secured
    @DgsData(parentType = "Page", field = "pageComponents")
    fun pageComponents(dfe: DgsDataFetchingEnvironment): List<PageComponentGraphQlView> {
        val page: PageGraphQlView = dfe.getSource()

        return pageComponents.findComponentsOfPage(UUID.fromString(page.id)).stream()
            .map { component -> PageComponentGraphQlView.from(component) }.toList()
    }
}