package com.hjusic.api.profileapi.accessRole.infrastructure

import com.hjusic.api.profileapi.accessRole.model.AccessRole

class AccessRoleDatabaseService {
    fun map(accessRole: AccessRole): AccessRoleDatabaseEntity {
        return AccessRoleDatabaseEntity(accessRole.accessRoleName, accessRole.accessRights)
    }

    fun map(accessRoleDatabaseEntity: AccessRoleDatabaseEntity): AccessRole {
        return AccessRole(accessRoleDatabaseEntity.id, accessRoleDatabaseEntity.accessRights)
    }
}