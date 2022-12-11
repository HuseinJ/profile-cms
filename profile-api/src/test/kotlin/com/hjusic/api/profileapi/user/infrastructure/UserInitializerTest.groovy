package com.hjusic.api.profileapi.user.infrastructure

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRoleName
import org.springframework.beans.factory.annotation.Autowired

class UserInitializerTest extends BaseSpringTest {

    @Autowired
    private UserDatabaseEntityRepository userDatabaseEntityRepository

    def "on server startup admin is initialized"() {
        when:
        def potentialAdmin = userDatabaseEntityRepository.findByName("admin")
        then:
        potentialAdmin.isPresent()
        potentialAdmin.get().roles.stream().anyMatch({ role -> role.id == AccessRoleName.ADMIN })
    }
}
