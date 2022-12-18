package com.hjusic.api.profileapi.user.infrastructure

import com.hjusic.api.profileapi.accessRole.infrastructure.AccessRoleDatabaseEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.*

@Document
data class UserDatabaseEntity(
    @Id
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val email: String,
    var password: String,
    @DBRef
    val roles: Set<AccessRoleDatabaseEntity>,
    val createdDate: Instant = Instant.now(),
    val modifiedDate: Instant = Instant.now(),



    )