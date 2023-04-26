package com.hjusic.api.profileapi.user.model

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.accessRole.model.AccessRoleService
import com.hjusic.api.profileapi.common.error.DomainErrorCode
import org.springframework.beans.factory.annotation.Autowired

class SignUpUserServiceTest extends BaseSpringTest {

    @Autowired
    private SignUpUserService createUserService;

    @Autowired
    private Users users

    def "should return userCreated Event with user and given values"() {
        given:
        def name = "somerandomNsefsefffefefame"
        def email = "some@mail.com"
        and:
        def callingUser = new User(UUID.randomUUID(), "calling", "admin", new HashSet<AccessRole>([AccessRoleService.adminRole()]), null)
        when:
        def userCreated = createUserService.createUserWithNameAndMail(name, "",email, callingUser);
        then:
        userCreated.success != null
        userCreated.success.user.name == name
        userCreated.success.user.email == email
    }

    def "should persist User if event is triggered"() {
        given:
        def name = "somerandomName"
        def email = "some@mail.com"
        and:
        def callingUser = new User(UUID.randomUUID(), "calling", "admin", new HashSet<AccessRole>([AccessRoleService.adminRole()]), null)
        and:
        def userCreated = createUserService.createUserWithNameAndMail(name, "",email, callingUser);
        when:
        users.trigger(userCreated.success)
        then:
        def persisteduser = users.findById(userCreated.success.user.id)
        persisteduser.name == name;
        persisteduser.email == email;
    }

    def "created User should have guest role"() {
        given:
        def name = "somerandomName1"
        def email = "some@mail1.com"
        and:
        def callingUser = new User(UUID.randomUUID(), "calling", "admin", new HashSet<AccessRole>([AccessRoleService.adminRole()]), null)
        and:
        def userCreated = createUserService.createUserWithNameAndMail(name, "",email, callingUser);
        when:
        users.trigger(userCreated.success)
        then:
        def persisteduser = users.findById(userCreated.success.user.id)
        persisteduser.name == name;
        persisteduser.email == email;
        persisteduser.roles.size() == 1
        persisteduser.roles.contains(AccessRoleService.guestRole())
    }

    def "should throw error if username already exists"() {
        given:
        def name = "somerandomNamsefsefe"
        def email = "some@mail.com"
        and:
        def callingUser = new User(UUID.randomUUID(), "calling", "admin", new HashSet<AccessRole>([AccessRoleService.adminRole()]), null)
        when:
        users.trigger(createUserService.createUserWithNameAndMail(name, "", email, callingUser).getSuccess())
        def result = createUserService.createUserWithNameAndMail(name, "","someOtherMail@test.com", callingUser);
        then:
        assert result.wasFailure()
        result.getFail().reason == UserDomainErrorCode.USER_NAME_IS_ALREADY_TAKEN.name()
    }
}
