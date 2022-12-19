package com.hjusic.api.profileapi.page.model

enum class PageDomainErrorCode(code: String) {
    NO_SUCH_PAGE_WITH_THIS_ID("no.such.page.with.this.id"),
    USER_IS_NOT_ALLOWED_TO_CREATE_PAGE("user.not.allowed.to.create.page"),
    USER_IS_NOT_ALLOWED_TO_DELETE_PAGE("user.not.allowed.to.delete.page"),
}