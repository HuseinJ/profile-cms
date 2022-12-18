package com.hjusic.api.profileapi.page.graphql

import com.hjusic.api.profileapi.page.model.Page

class PageGraphQlView private constructor(
    val id: String,
    val name: String
){

    companion object{
        fun from(page: Page): PageGraphQlView{
            return PageGraphQlView(page.id.toString(), page.name);
        }
    }
}