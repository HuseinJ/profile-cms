package com.hjusic.api.profileapi.user.model

import com.hjusic.api.profileapi.user.infrastructure.UserDatabaseEntity
import java.util.*

interface Users {
    fun trigger(userEvent: UserEvent): User
    fun findById(userId: UUID): User
    fun findAll(): MutableList<User>
    fun findByName(username: String): User
    fun existsByName(name: String): Boolean
    fun existsByemail(email: String): Boolean

    fun map(user: UserDatabaseEntity): User
}