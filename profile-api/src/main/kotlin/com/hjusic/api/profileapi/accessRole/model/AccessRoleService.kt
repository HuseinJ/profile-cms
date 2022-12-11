package com.hjusic.api.profileapi.accessRole.model

class AccessRoleService {
    companion object {

        @JvmStatic
        fun adminRole(): AccessRole{
            return AccessRole(AccessRoleName.ADMIN, setOf(AccessRight.CREATE_USER, AccessRight.DELETE_USER))
        }

        @JvmStatic
        fun guestRole(): AccessRole {
            return AccessRole(AccessRoleName.GUEST, setOf())
        }
    }

}