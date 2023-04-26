package com.hjusic.api.profileapi.user.application

import com.hjusic.api.profileapi.BaseSpringTest
import com.hjusic.api.profileapi.accessRole.model.AccessRole
import com.hjusic.api.profileapi.accessRole.model.AccessRoleService
import com.hjusic.api.profileapi.common.error.ValidationErrorCode
import com.hjusic.api.profileapi.user.model.SignUpUserService
import com.hjusic.api.profileapi.user.model.User
import com.hjusic.api.profileapi.user.model.Users
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder

class SignInUserTest extends BaseSpringTest {

    @Autowired
    private SignUpUserService signUpUserService

    @Autowired
    private Users users

    @Autowired
    private SignInUser signInUser

    @Autowired
    private PasswordEncoder passwordEncoder

    def "should return user and token when user signs in"() {
        given:
        def username = "name"
        def password = "supersecure123"
        and:
        users.trigger(signUpUserService.createUserWithNameAndMail(username, passwordEncoder.encode(password), "test@mail.com",
                new User(UUID.randomUUID(), "admin", "email@amdin.com",
                        new HashSet<AccessRole>([AccessRoleService.adminRole()]), null)).success)
        when:
        def result = signInUser.signInUser(username, password)
        then:
        result.wasSuccess();
        def success = result.getSuccess()
        success.user.name == username
        success.token != ""
        success.token.length() > 0
    }

    def "should throw error if user tries wrong credentials"() {
        given:
        def username = "name2"
        def password = "supersecur22e123"
        and:
        users.trigger(signUpUserService.createUserWithNameAndMail(username, passwordEncoder.encode(password), "te22st@mail.com",
                new User(UUID.randomUUID(), "admin", "ema22il@amdin.com",
                        new HashSet<AccessRole>([AccessRoleService.adminRole()]), null)).success)
        when:
        def result = signInUser.signInUser(username, "falsePassword")
        then:
        result.wasFailure();
        def fail = result.getFail()
        fail.reason == ValidationErrorCode.WRONG_CREDENTIALS.name()
    }
}
