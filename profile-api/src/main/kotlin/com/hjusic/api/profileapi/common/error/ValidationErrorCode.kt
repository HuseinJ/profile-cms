package com.hjusic.api.profileapi.common.error

enum class ValidationErrorCode(val code: String) {
    EMPTY_VALUE("empty.value"),
    WRONG_CREDENTIALS("wrong.credentials"),
    VALUES_DO_NOT_MATCH("values.do.not.match"),
    EXCEPTION("exception"),
}