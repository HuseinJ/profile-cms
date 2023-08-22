package com.hjusic.api.profileapi.accessRole.model

class AccessRoleService {
    companion object {

        @JvmStatic
        fun adminRole(): AccessRole {
            return AccessRole(
                AccessRoleName.ROLE_ADMIN,
                setOf(
                    AccessRight.CREATE_USER,
                    AccessRight.ASSIGN_HOMEPAGE,
                    AccessRight.LIST_USER,
                    AccessRight.DELETE_USER,
                    AccessRight.CREATE_PAGE,
                    AccessRight.MODIFY_PAGE,
                )
            )
        }

        @JvmStatic
        fun guestRole(): AccessRole {
            return AccessRole(AccessRoleName.ROLE_GUEST, setOf())
        }
    }

}