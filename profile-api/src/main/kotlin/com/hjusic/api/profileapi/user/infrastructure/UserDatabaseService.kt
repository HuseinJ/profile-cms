package com.hjusic.api.profileapi.user.infrastructure

import com.hjusic.api.profileapi.accessRole.infrastructure.AccessRoleDatabaseService
import com.hjusic.api.profileapi.common.event.EventPublisher
import com.hjusic.api.profileapi.user.model.*
import java.util.*
import java.util.stream.Collectors
import kotlin.streams.toList

class UserDatabaseService(
    private val eventPublisher: EventPublisher,
    private val userDatabaseEntityRepository: UserDatabaseEntityRepository,
    private val accessRoleDatabaseService: AccessRoleDatabaseService
) : Users {
    override fun trigger(userEvent: UserEvent): User {
        var user = User(UUID.randomUUID(), "test", "test@test.com", setOf())

        when (userEvent) {
            is UserCreated -> user = handle(userEvent)
            is PasswordChanged -> user = handle(userEvent)
        }

        eventPublisher.publish(userEvent)

        return user
    }

    override fun findById(userId: UUID): User {
        var userentity = userDatabaseEntityRepository.findById(userId)
        if (userentity.isEmpty) {
            throw NoSuchElementException()
        }
        return map(userentity.get())
    }

    override fun findAll(): MutableList<User> {
        return userDatabaseEntityRepository.findAll().stream().map { user -> map(user) }.toList();
    }

    override fun findByName(username: String): User {
        var userentity = userDatabaseEntityRepository.findByName(username)
        if (userentity.isEmpty) {
            throw NoSuchElementException()
        }
        return map(userentity.get())
    }

    override fun existsByName(name: String): Boolean {
        return userDatabaseEntityRepository.existsByName(name)
    }

    override fun existsByemail(email: String): Boolean {
        return userDatabaseEntityRepository.existsByEmail(email)
    }

    private fun handle(userCreated: UserCreated): User {
        return map(
            userDatabaseEntityRepository.save(
                UserDatabaseEntity(
                    userCreated.user.id,
                    userCreated.user.name,
                    userCreated.user.email,
                    userCreated.password,
                    userCreated.user.roles.stream().map { role -> accessRoleDatabaseService.map(role) }.collect(
                        Collectors.toSet()
                    )
                )
            )
        )
    }

    private fun handle(passwordChanged: PasswordChanged): User {
        var user = userDatabaseEntityRepository.findById(passwordChanged.user.id).orElseThrow{IllegalStateException("user not in database")}
        user.password = passwordChanged.newPassword;
        return map(
            userDatabaseEntityRepository.save(user)
        )
    }
    override fun map(userDatabaseEntity: UserDatabaseEntity): User {
        return User(
            userDatabaseEntity.id,
            userDatabaseEntity.name,
            userDatabaseEntity.email,
            userDatabaseEntity.roles.stream().map { role -> accessRoleDatabaseService.map(role) }.collect(
                Collectors.toSet()
            )
        )
    }
}