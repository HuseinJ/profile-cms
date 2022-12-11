package com.hjusic.api.profileapi.user.model

class UserCreated(user: User, val password: String) : UserEvent(user) {
}