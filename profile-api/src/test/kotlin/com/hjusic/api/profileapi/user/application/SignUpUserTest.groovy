package com.hjusic.api.profileapi.user.application

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.accessRole.model.AccessRoleName
import com.hjusic.api.profileapi.accessRole.model.AccessRoleService
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.user.infrastructure.UserDatabaseEntityRepository
import com.hjusic.api.profileapi.user.model.User
import org.springframework.beans.factory.annotation.Autowired

class SignUpUserTest extends BaseSpringTest {

    @Autowired
    private SignUpUser createUser

    @Autowired
    private UserDatabaseEntityRepository userDatabaseEntityRepository

    def "should throw error if email is empty"() {
        when:
        def result = createUser.createUserWithNameAndMail("name", "","")
        then:
        result.getFail().reason == ValidationErrorCode.EMPTY_VALUE.name()
    }

    def "should throw error if name is empty"() {
        when:
        def result = createUser.createUserWithNameAndMail("", "","test@test.com")
        then:
        result.getFail().reason == ValidationErrorCode.EMPTY_VALUE.name()
    }

    def "should throw error if password is empty"() {
        when:
        def result = createUser.createUserWithNameAndMail("sef", "","test@test.com")
        then:
        result.getFail().reason == ValidationErrorCode.EMPTY_VALUE.name()
    }

    def "should encrypt password"() {
        given:
        def name= "user-password-test"
        def password = "thisIsClearText"
        def email = "testlmsef@mail.com"
        and:
        userAuthServices.callingUser() >> new User(UUID.randomUUID(), name, email, new HashSet<AccessRole>([AccessRoleService.adminRole()]), null)
        when:
        def result = createUser.createUserWithNameAndMail(name, password,email)
        then:
        assert result.wasSuccess()
        def user = userDatabaseEntityRepository.findByName(name)
        assert user.isPresent()
        user.get().password != password

    }

    def "should return User with given name and email"() {
        given:
        def name = "naeeeefeme"
        def email = "test@test.com"
        and:
        userAuthServices.callingUser() >> new User(UUID.randomUUID(), name, email, new HashSet<AccessRole>([AccessRoleService.adminRole()]), null)
        when:
        def result = createUser.createUserWithNameAndMail(name, "sef" ,email)
        then:
        def user = result.getSuccess()
        user.email == email
        user.name == name
    }

    def "created user should only have guest role"() {
        given:
        def name = "namffee1"
        def email = "test1@test.com"
        and:
        userAuthServices.callingUser() >> new User(UUID.randomUUID(), "calling", "admin", new HashSet<AccessRole>([AccessRoleService.adminRole()]),null)
        when:
        def result = createUser.createUserWithNameAndMail(name, "ffef",email)
        then:
        def user = result.getSuccess()
        user.roles.size() == 1
        user.roles.first().accessRoleName == AccessRoleName.ROLE_GUEST
    }
}
