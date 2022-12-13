package com.hjusic.api.profileapi.accessRole.infrastructure

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRoleName
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

class AccessRoleInitializerTest extends BaseSpringTest {

    @Autowired
    private AccessRoleDatabaseEntityRepository accessRoleDatabaseEntityRepository

    def "should create roles on server startup"() {
        given:
        def roleNames = [AccessRoleName.ROLE_ADMIN, AccessRoleName.ROLE_GUEST]
        when:
        def accessRoles = accessRoleDatabaseEntityRepository.findAll()
        then:
        accessRoles.size() > 0
        accessRoles.stream().allMatch({ role -> roleNames.contains(role.id) })
    }
}
