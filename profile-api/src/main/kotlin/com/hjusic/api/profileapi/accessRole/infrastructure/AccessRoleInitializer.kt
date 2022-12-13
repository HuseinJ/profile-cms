package com.hjusic.api.profileapi.accessRole.infrastructure

import com.hjusic.api.profileapi.accessRole.model.AccessRoleName
import com.hjusic.api.profileapi.accessRole.model.AccessRoleService
import com.hjusic.api.profileapi.accessRole.model.AccessRolesInitialized
import com.hjusic.api.profileapi.common.event.EventPublisher
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener

class AccessRoleInitializer(
    val accessRoleDatabaseEntityRepository: AccessRoleDatabaseEntityRepository,
    val accessRoleDatabaseService: AccessRoleDatabaseService,
    val eventPublisher: EventPublisher
) {

    @EventListener
    fun appReadyAdminRole(event: ApplicationReadyEvent) {
        if(accessRoleDatabaseEntityRepository.findById(AccessRoleName.ROLE_ADMIN).isEmpty){
            accessRoleDatabaseEntityRepository.save(accessRoleDatabaseService.map(AccessRoleService.adminRole()))
        }
        if(accessRoleDatabaseEntityRepository.findById(AccessRoleName.ROLE_GUEST).isEmpty){
            accessRoleDatabaseEntityRepository.save(accessRoleDatabaseService.map(AccessRoleService.guestRole()))
        }

        eventPublisher.publish(AccessRolesInitialized())
    }
}