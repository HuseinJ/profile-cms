package com.hjusic.api.profileapi.common.error

enum class ValidationErrorCode(val code: String) {
    EMPTY_VALUE("empty.value"),
    WRONG_FORMAT("wrong.format"),
    WRONG_CREDENTIALS("wrong.credentials"),
    EXPIRED_CREDENTIALS("expired.credentials"),
    VALUES_DO_NOT_MATCH("values.do.not.match"),
    EXCEPTION("exception"),
    ACTION_NOT_ALLOWED_FOR_OBJECT("action.not.allowed.for.object")
}