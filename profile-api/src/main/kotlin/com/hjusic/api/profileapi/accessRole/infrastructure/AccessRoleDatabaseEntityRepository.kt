package com.hjusic.api.profileapi.accessRole.infrastructure

import com.hjusic.api.profileapi.accessRole.model.AccessRoleName
import org.springframework.data.mongodb.repository.MongoRepository

interface AccessRoleDatabaseEntityRepository: MongoRepository<AccessRoleDatabaseEntity, AccessRoleName> {
}