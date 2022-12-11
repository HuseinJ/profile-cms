package com.hjusic.api.profileapi.accessRole.infrastructure

import com.hjusic.api.profileapi.common.event.EventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AccessRoleConfiguration {

    @Bean
    fun accessRoleDatabaseService(): AccessRoleDatabaseService {
        return AccessRoleDatabaseService()
    }

    @Bean
    fun accessRoleInitializer(
        accessRoleDatabaseEntityRepository: AccessRoleDatabaseEntityRepository,
        accessRoleDatabaseService: AccessRoleDatabaseService,
        eventPublisher: EventPublisher
    ): AccessRoleInitializer {
        return AccessRoleInitializer(accessRoleDatabaseEntityRepository, accessRoleDatabaseService, eventPublisher)
    }
}