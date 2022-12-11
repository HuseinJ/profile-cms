package com.hjusic.api.profileapi.accessRole.model

import spock.lang.Specification


class AccessRoleServiceTest extends Specification {

    def "admin role should contain rights"() {
        when:
        def adminRole = AccessRoleService.adminRole()
        then:
        adminRole.accessRoleName == AccessRoleName.ADMIN
        adminRole.accessRights.containsAll(
                Set.of(AccessRight.CREATE_USER, AccessRight.DELETE_USER)
        )
    }

    def "guest role should contain rights no"() {
        when:
        def guestRole = AccessRoleService.guestRole()
        then:
        guestRole.accessRoleName == AccessRoleName.GUEST
        guestRole.accessRights.containsAll(
                Set.of()
        )
    }
}
