package com.hjusic.api.profileapi.user.model

enum class UserDomainErrorCode(code: String){
    USER_IS_NOT_PERMITTED_TO_CREATE_USERS("user.is.not.permitted.to.create.users"),
    USER_NAME_IS_ALREADY_TAKEN("user.name.is.already.taken"),
    USER_MAIL_IS_ALREADY_TAKEN("user.mail.is.already.taken"),
}