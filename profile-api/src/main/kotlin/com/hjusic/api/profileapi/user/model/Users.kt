package com.hjusic.api.profileapi.user.model

import java.util.*

interface Users {
    fun trigger(userEvent: UserEvent): User
    fun findById(userId: UUID): User
    fun findByName(username: String): User
    fun existsByName(name: String): Boolean
    fun existsByemail(email: String): Boolean
}