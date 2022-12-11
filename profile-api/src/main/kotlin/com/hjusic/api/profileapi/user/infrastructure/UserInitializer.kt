package com.hjusic.api.profileapi.user.infrastructure

import com.hjusic.api.profileapi.accessRole.infrastructure.AccessRoleDatabaseEntityRepository
import com.hjusic.api.profileapi.accessRole.model.AccessRoleName
import com.hjusic.api.profileapi.accessRole.model.AccessRolesInitialized
import org.springframework.context.event.EventListener
import java.util.*

class UserInitializer(
    val userDatabaseEntityRepository: UserDatabaseEntityRepository,
    val accessRoleDatabaseEntityRepository: AccessRoleDatabaseEntityRepository
) {

    @EventListener
    fun initializeAdmin(event: AccessRolesInitialized) {
        var potentialUser = userDatabaseEntityRepository.findByName("admin")
        var potentialAccessRole = accessRoleDatabaseEntityRepository.findById(AccessRoleName.ADMIN)

        if(potentialAccessRole.isEmpty){
            throw IllegalStateException("AccessRole Admin was not initialized")
        }

        //TODO: INTIALIZE ADMIN WITH RIGHT PASSWORD !!

        if(potentialUser.isEmpty){
            userDatabaseEntityRepository.save(UserDatabaseEntity(UUID.randomUUID(),"admin","admin@localhost", "",setOf(potentialAccessRole.get())))
        }
    }
}