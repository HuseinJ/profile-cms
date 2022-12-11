package com.hjusic.api.profileapi.accessRole.model

data class AccessRole(
    val accessRoleName: AccessRoleName,
    val accessRights: Set<AccessRight>
) {
}