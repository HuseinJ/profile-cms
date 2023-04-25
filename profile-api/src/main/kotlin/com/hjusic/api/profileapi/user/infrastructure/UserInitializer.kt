package com.hjusic.api.profileapi.user.infrastructure

import com.hjusic.api.profileapi.accessRole.infrastructure.AccessRoleDatabaseEntityRepository
import com.hjusic.api.profileapi.accessRole.model.AccessRoleName
import com.hjusic.api.profileapi.accessRole.model.AccessRolesInitialized
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.event.EventListener
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant
import java.util.*

class UserInitializer(
    val userDatabaseEntityRepository: UserDatabaseEntityRepository,
    val accessRoleDatabaseEntityRepository: AccessRoleDatabaseEntityRepository,
    val passwordEncoder: PasswordEncoder
) {

    @Value("\${auth.admin.password}")
    private val adminPassword: String = ""

    @Value("\${auth.admin.email}")
    private val adminEmail: String = ""

    @EventListener
    fun initializeAdmin(event: AccessRolesInitialized) {
        var potentialUser = userDatabaseEntityRepository.findByName("admin")
        var potentialAccessRole = accessRoleDatabaseEntityRepository.findById(AccessRoleName.ROLE_ADMIN)

        if (potentialAccessRole.isEmpty) {
            throw IllegalStateException("AccessRole Admin was not initialized")
        }

        if (potentialUser.isEmpty) {
            userDatabaseEntityRepository.save(
                UserDatabaseEntity(
                    UUID.randomUUID(),
                    "admin",
                    adminEmail,
                    passwordEncoder.encode(adminPassword),
                    setOf(potentialAccessRole.get()),
                    RefreshTokenDatabaseEntity(UUID.randomUUID(), "", Instant.now())
                )
            )
        }
    }
}