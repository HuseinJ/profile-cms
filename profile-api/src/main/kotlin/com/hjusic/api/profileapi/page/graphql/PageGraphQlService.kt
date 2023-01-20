package com.hjusic.api.profileapi.page.graphql

import com.hjusic.api.profileapi.page.application.GetPage
import com.hjusic.api.profileapi.page.model.Pages
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import org.springframework.security.access.annotation.Secured
import java.util.UUID

@DgsComponent
class PageGraphQlService(
    val pages: Pages,
    val getPage: GetPage
) {

    @DgsQuery
    @Secured
    fun page(@InputArgument("uuid") uuid: String): PageGraphQlView? {
        var result = getPage.getPage(uuid)

        if(result.wasFailure()){
            throw java.lang.RuntimeException(result.fail?.reason);
        }

        if (result.wasSuccess() && result.success?.isEmpty()!!){
            return null;
        }

        return PageGraphQlView.from(result.success!!.get());
    }

    @DgsQuery
    @Secured
    fun pages(): List<PageGraphQlView> {
        return pages.findAll().stream().map { page -> PageGraphQlView.from(page) }.toList();
    }
}