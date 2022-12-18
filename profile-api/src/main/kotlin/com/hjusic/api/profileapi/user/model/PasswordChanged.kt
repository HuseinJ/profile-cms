package com.hjusic.api.profileapi.user.model

class PasswordChanged private constructor(user: User, val newPassword: String) : UserEvent(user){
    companion object {
        fun from(user: User, newPassword: String): PasswordChanged{
            return PasswordChanged(user, newPassword);
        }
    }
}