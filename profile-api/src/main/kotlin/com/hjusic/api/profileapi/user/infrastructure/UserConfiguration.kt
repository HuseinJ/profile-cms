package com.hjusic.api.profileapi.user.infrastructure

import com.hjusic.api.profileapi.accessRole.infrastructure.AccessRoleDatabaseEntityRepository
import com.hjusic.api.profileapi.accessRole.infrastructure.AccessRoleDatabaseService
import com.hjusic.api.profileapi.common.event.EventPublisher
import com.hjusic.api.profileapi.common.security.UserAuthServiceImpl
import com.hjusic.api.profileapi.common.security.UserAuthServices
import com.hjusic.api.profileapi.common.security.util.JwtUtils
import com.hjusic.api.profileapi.user.application.ChangePassword
import com.hjusic.api.profileapi.user.application.RefreshTokenOfUser
import com.hjusic.api.profileapi.user.application.SignInUser
import com.hjusic.api.profileapi.user.application.SignUpUser
import com.hjusic.api.profileapi.user.model.SignUpUserService
import com.hjusic.api.profileapi.user.model.Users
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class UserConfiguration {

    @Bean
    fun userDatabaseService(
        eventPublisher: EventPublisher,
        userDatabaseEntityRepository: UserDatabaseEntityRepository,
        refreshTokenDatabaseEntityRepository: RefreshTokenDatabaseEntityRepository,
        accessRoleDatabaseService: AccessRoleDatabaseService
    ): Users {
        return UserDatabaseService(
            eventPublisher,
            userDatabaseEntityRepository,
            refreshTokenDatabaseEntityRepository,
            accessRoleDatabaseService
        )
    }

    @Bean
    fun createUserService(users: Users): SignUpUserService {
        return SignUpUserService(users)
    }

    @Bean
    fun userAuthServices(): UserAuthServices {
        return UserAuthServiceImpl()
    }

    @Bean
    fun signUpUser(
        users: Users,
        signUpUserService: SignUpUserService,
        userAuthServices: UserAuthServices,
        passwordEncoder: PasswordEncoder
    ): SignUpUser {
        return SignUpUser(signUpUserService, users, userAuthServices, passwordEncoder)
    }

    @Bean
    fun signInUser(authenticationManager: AuthenticationManager, users: Users, jwtUtils: JwtUtils): SignInUser {
        return SignInUser(authenticationManager, users, jwtUtils)
    }

    @Bean
    fun refreshTokenOfUser(users: Users, jwtUtils: JwtUtils): RefreshTokenOfUser {
        return RefreshTokenOfUser(users, jwtUtils)
    }

    @Bean
    fun userInitializer(
        userDatabaseEntityRepository: UserDatabaseEntityRepository,
        accessRoleDatabaseEntityRepository: AccessRoleDatabaseEntityRepository,
        passwordEncoder: PasswordEncoder
    ): UserInitializer {
        return UserInitializer(userDatabaseEntityRepository, accessRoleDatabaseEntityRepository, passwordEncoder)
    }

    @Bean
    fun changePassword(
        authenticationManager: AuthenticationManager,
        users: Users,
        passwordEncoder: PasswordEncoder
    ): ChangePassword {
        return ChangePassword(authenticationManager, users, passwordEncoder)
    }
}