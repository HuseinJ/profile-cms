package com.hjusic.api.profileapi.user.infrastructure

import com.hjusic.api.profileapi.accessRole.infrastructure.AccessRoleDatabaseService
import com.hjusic.api.profileapi.common.event.EventPublisher
import com.hjusic.api.profileapi.user.model.*
import java.util.*
import java.util.stream.Collectors

class UserDatabaseService(
    private val eventPublisher: EventPublisher,
    private val userDatabaseEntityRepository: UserDatabaseEntityRepository,
    private val refreshTokenDatabaseEntityRepository: RefreshTokenDatabaseEntityRepository,
    private val accessRoleDatabaseService: AccessRoleDatabaseService
) : Users {

    override fun trigger(userEvent: UserEvent): User {
        var user =  when (userEvent) {
            is UserCreated -> handle(userEvent)
            is PasswordChanged -> handle(userEvent)
            is RefreshTokenCreated -> handle(userEvent)
            else -> {
                throw UnsupportedOperationException("This Event can not be handled")
            }
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
        return userDatabaseEntityRepository.findAll().stream().map { user -> map(user) }.toList()
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

    override fun findRefreshTokenOfUser(user: User): RefreshToken? {

        var user = userDatabaseEntityRepository.findById(user.id).orElseThrow { IllegalArgumentException() }

        if (user.refreshTokenDatabaseEntity != null) {
            return RefreshToken.from(user.refreshTokenDatabaseEntity!!)
        }
        return null
    }

    override fun findUserByRefreshToken(refreshToken: String): User? {
        var user = userDatabaseEntityRepository.findByRefreshTokenDatabaseEntityToken(refreshToken)

        if (user.isEmpty) {
            return null
        }
        return map(user.get());
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
                    ),
                null
                )
            )
        )
    }

    private fun handle(refreshTokenCreated: RefreshTokenCreated): User {

        val possibleUser = userDatabaseEntityRepository.findById(refreshTokenCreated.user.id)

        if (possibleUser.isEmpty) {
            throw java.lang.IllegalArgumentException("user can not be null")
        }

        var user = possibleUser.get()

        user.refreshTokenDatabaseEntity = refreshTokenDatabaseEntityRepository.save(
            RefreshTokenDatabaseEntity(
                refreshTokenCreated.refreshToken.token,
                refreshTokenCreated.refreshToken.expirationDate
            )
        )

        return map(
            userDatabaseEntityRepository.save(
                user
            )
        )
    }

    private fun handle(passwordChanged: PasswordChanged): User {
        var user = userDatabaseEntityRepository.findById(passwordChanged.user.id)
            .orElseThrow { IllegalStateException("user not in database") }
        user.password = passwordChanged.newPassword
        return map(
            userDatabaseEntityRepository.save(user)
        )
    }

    override fun map(userDatabaseEntity: UserDatabaseEntity): User {
        val refreshToken = if (userDatabaseEntity.refreshTokenDatabaseEntity != null) {
            RefreshToken(
                userDatabaseEntity.refreshTokenDatabaseEntity!!.token,
                userDatabaseEntity.refreshTokenDatabaseEntity!!.expiryDate
            )
        } else {
            null
        }

        return User(
            userDatabaseEntity.id,
            userDatabaseEntity.name,
            userDatabaseEntity.email,
            userDatabaseEntity.roles.stream().map { role -> accessRoleDatabaseService.map(role) }.collect(
                Collectors.toSet()
            ),
            refreshToken
        )
    }
}