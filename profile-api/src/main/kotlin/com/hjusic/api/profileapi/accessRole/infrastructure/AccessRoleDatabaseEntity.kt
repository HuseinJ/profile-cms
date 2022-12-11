package com.hjusic.api.profileapi.accessRole.infrastructure

import com.hjusic.api.profileapi.accessRole.model.AccessRight
import com.hjusic.api.profileapi.accessRole.model.AccessRoleName
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class AccessRoleDatabaseEntity(
    @Id
    val id: AccessRoleName,
    val accessRights: Set<AccessRight>
)